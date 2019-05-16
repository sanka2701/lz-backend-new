package sk.liptovzije.core.service.potw;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.photo.QWeeklyPhoto;
import sk.liptovzije.application.photo.WeeklyPhoto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultPhotoOfTheWeekService implements PhotoOfTheWeekService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<WeeklyPhoto> save(WeeklyPhoto photo) {
        entityManager.unwrap(Session.class).save(photo);
        return Optional.of(photo);
    }

    @Override
    @Transactional
    public void update(WeeklyPhoto photo) {
        entityManager.unwrap(Session.class).update(photo);
    }

    @Override
    @Transactional
    public Optional<WeeklyPhoto> getById(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QWeeklyPhoto photo = QWeeklyPhoto.weeklyPhoto;
        WeeklyPhoto result = query.selectFrom(photo)
                .where(photo.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    @Transactional
    public List<WeeklyPhoto> getAll() {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QWeeklyPhoto photo = QWeeklyPhoto.weeklyPhoto;
        return query.selectFrom(photo).fetch();
    }

    @Override
    @Transactional
    public void delete(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QWeeklyPhoto photo = QWeeklyPhoto.weeklyPhoto;
        query.delete(photo).where(photo.id.eq(id)).execute();
    }
}
