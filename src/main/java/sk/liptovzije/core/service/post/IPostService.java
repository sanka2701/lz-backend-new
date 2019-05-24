package sk.liptovzije.core.service.post;

import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.file.File;
import sk.liptovzije.application.post.Post;

import java.util.Set;

public interface IPostService {
    Post resolveFileDependencies(Post post,
                                 MultipartFile thumbnail,
                                 String[] fileUrls,
                                 MultipartFile[] files);

    Set<File> updateContentFiles(Post original, Post updated);

    void removeUnusedFiles(Set<File> toBeDeleted);
}
