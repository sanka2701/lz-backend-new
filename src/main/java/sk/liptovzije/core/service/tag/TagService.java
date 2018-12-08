package sk.liptovzije.core.service.tag;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.tag.EventTag;
import sk.liptovzije.application.tag.QEventTag;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TagService implements ITagService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<EventTag> save(EventTag tag) {
        try {
            entityManager.unwrap(Session.class).save(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(tag);
    }

    @Override
    public void update(EventTag updatedTag) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEventTag tag = QEventTag.eventTag;
        query.update(tag).where(tag.id.eq(updatedTag.getId())).execute();
    }

    @Override
    public void delete(long id) {
        //todo: remove category reference from every event where it is used
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEventTag tag = QEventTag.eventTag;
        query.delete(tag).where(tag.id.eq(id)).execute();
    }

    @Override
    public Optional<EventTag> getById(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEventTag tag = QEventTag.eventTag;
        EventTag result = query.selectFrom(tag)
                .where(tag.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<EventTag> getByLabel(String label) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEventTag tag = QEventTag.eventTag;
        EventTag result = query.selectFrom(tag)
                .where(tag.label.eq(label))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<EventTag> getAll() {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEventTag tag = QEventTag.eventTag;
        return query.selectFrom(tag).fetch();
    }
}
