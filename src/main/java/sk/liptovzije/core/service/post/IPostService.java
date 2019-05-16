package sk.liptovzije.core.service.post;

import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.post.Post;

public interface IPostService {
    Post resolveFileDependencies(Post post,
                                 MultipartFile thumbnail,
                                 String[] fileUrls,
                                 MultipartFile[] files);
}
