package sk.liptovzije.core.service.event;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.event.QEvent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<Event> create(Event event) {
        try {
            entityManager.unwrap(Session.class).save(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(event);
    }

    @Override
    @Transactional
    public void delete(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEvent event = QEvent.event;
        query.delete(event).where(event.id.eq(id)).execute();
    }

    @Override
    @Transactional
    public void update(Event event) {
        entityManager.unwrap(Session.class).update(event);
    }

    @Override
    public Optional<Event> getById(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEvent event = QEvent.event;
        Event result = query.selectFrom(event)
                .where(event.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Event> getAll() {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEvent event = QEvent.event;
        return query.selectFrom(event).fetch();
    }
}
