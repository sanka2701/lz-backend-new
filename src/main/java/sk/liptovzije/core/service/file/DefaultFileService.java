package sk.liptovzije.core.service.file;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.file.File;
import sk.liptovzije.application.file.QFile;
import sk.liptovzije.core.service.storage.IStorageService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultFileService implements IFileService {
    @PersistenceContext
    private EntityManager entityManager;
    private IStorageService storageService;

    @Autowired
    public DefaultFileService(IStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Optional<File> save(MultipartFile mediaFile) {
        File file = null;

        try {
            file = storageService.store(mediaFile);
            entityManager.unwrap(Session.class).save(file);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(file);
    }

    @Override
    public List<File> save(Collection<MultipartFile> files) {
        return files.stream()
                .map(this::save)
                .flatMap(file -> file.map(Stream::of).orElseThrow(InternalError::new))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(File file) {
        //todo: handle properly and pass Path instead of string
        try {
            storageService.delete(file.getPath().toString());
        } catch (Exception e) {

        }

        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QFile qFile= QFile.file;
        query.delete(qFile).where(qFile.id.eq(file.getId())).execute();
    }

    @Override
    public void delete(Collection<File> files) {
        files.forEach(this::delete);
    }
}
