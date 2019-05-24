package sk.liptovzije.core.service.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.file.File;
import sk.liptovzije.application.post.Post;
import sk.liptovzije.core.service.IFileUrlBuilder;
import sk.liptovzije.core.service.file.IFileService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultPostService implements IPostService {

    private IFileUrlBuilder pathBuilder;
    private IFileService fileService;

    @Autowired
    public DefaultPostService(IFileService fileService, IFileUrlBuilder pathBuilder) {
        this.fileService = fileService;
        this.pathBuilder = pathBuilder;
    }

    @Override
    public Post resolveFileDependencies(Post post, MultipartFile thumbnail, String[] fileUrls, MultipartFile[] files) {
        if (thumbnail != null) {
            File thumbnailFile = fileService.save(thumbnail).orElseThrow(InternalError::new);
            post.setThumbnail(thumbnailFile);
        }

        if(fileUrls != null && files != null) {
            List<String> contentFileUrls = Arrays.asList(fileUrls);
            List<MultipartFile> urlFiles = Arrays.asList(files);
            List<File> contentFiles = fileService.save(urlFiles);

            Map<String, File> urlMap = pathBuilder.buildFileUrlMap(contentFileUrls, contentFiles);
            post.setContent(pathBuilder.replaceUrls(post.getContent(), urlMap));
            post.setFiles(new HashSet<>(urlMap.values()));
        }

        return post;
    }

    @Override
    public Set<File> updateContentFiles(Post original, Post updated) {
        Set<File> allFiles = new HashSet<File>(){{
            addAll(original.getFiles());
            addAll(updated.getFiles());
        }};

        Set<File> present = allFiles
                .stream()
                .filter(file -> updated.getContent().contains(file.getName()))
                .collect(Collectors.toSet());

        // allFiles now hold only files that should be deleted - removeAll returns boolean instead of expected set
        allFiles.removeAll(present);
        Set<File> toBeDeleted = new HashSet<>(allFiles);

        if(updated.getThumbnail() != null) {
            toBeDeleted.add(original.getThumbnail());
            original.setThumbnail(updated.getThumbnail());
        }
        original.setFiles(present);

        return toBeDeleted;
    }

    @Override
    public void removeUnusedFiles(Set<File> toBeDeleted) {
        List<Long> ids = toBeDeleted
                .stream()
                .map(File::getId)
                .collect(Collectors.toList());
        fileService.delete(ids);
    }
}
