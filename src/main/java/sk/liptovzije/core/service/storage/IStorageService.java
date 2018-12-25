package sk.liptovzije.core.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.file.File;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    void init();
    void delete(String filePath) throws IOException;
    File store(MultipartFile file);
    Stream<Path> loadAll();
    Resource loadAsResource(String filename);
    void deleteAll();
}
