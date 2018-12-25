package sk.liptovzije.core.service.storage;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.file.File;
import sk.liptovzije.utils.exception.StorageException;
import sk.liptovzije.utils.exception.StorageFileNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageService implements IStorageService {
    private final Path rootLocation;

    @Autowired
    public StorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public File store(MultipartFile file) {
        File storedFile = new File();
        try {
            storedFile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
            storedFile.setName(generateFileName());
            storedFile.setDirectory(resolveCurrentDirectory().toString());

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty storage " + storedFile);
            }

            Files.copy(file.getInputStream(),
                    toRootLocation(storedFile.getPath()),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store storage " + storedFile, e);
        }

        return storedFile;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = toRootLocation(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read storage: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read storage: " + filename, e);
        }
    }

    @Override
    public void delete(String filePath) throws IOException {
        Path path = rootLocation.resolve(filePath);
        Files.delete(path);
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    private Path toRootLocation(String filename) {
        return rootLocation.resolve(filename);
    }

    private Path toRootLocation(Path path) {
        return rootLocation.resolve(path);
    }

    private String generateFileName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private Path resolveCurrentDirectory() throws IOException {
        DateTime now = DateTime.now();
        String year  =  String.valueOf(now.getYear());
        String month =  String.valueOf(now.getMonthOfYear());
        String day   =  String.valueOf(now.getDayOfMonth());

        Path dateSubPath = Paths.get(year, month, day);
        Path finalPath   = rootLocation.resolve(dateSubPath);

        if(!Files.exists(finalPath)) {
            Files.createDirectories(finalPath);
        }

        return dateSubPath;
    }
}
