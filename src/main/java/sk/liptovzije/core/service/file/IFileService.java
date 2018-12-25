package sk.liptovzije.core.service.file;

import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.file.File;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IFileService {
    Optional<File> save(MultipartFile file);
    List<File> save(Collection<MultipartFile> files);
    void delete(File file);
    void delete(Collection<File> files);
}
