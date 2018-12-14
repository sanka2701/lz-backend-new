package sk.liptovzije.core.service.place;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.place.Place;
import sk.liptovzije.application.place.QPlace;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PlaceService implements IPlaceService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Place> save(Place place) {
        try {
            entityManager.unwrap(Session.class).save(place);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(place);
    }

    @Override
    public Optional<Long>  update(Place updatedPlace) {
        // todo: make functional
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QPlace place = QPlace.place;
        Long id = query.update(place).where(place.id.eq(updatedPlace.getId())).execute();

        return Optional.of(id);
    }

    @Override
    public void delete(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QPlace place = QPlace.place;
        query.delete(place).where(place.id.eq(id)).execute();
    }

    @Override
    public Optional<Place> getById(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QPlace place = QPlace.place;
        Place result = query.selectFrom(place)
                .where(place.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Place> getBySubstring(String subName) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QPlace place = QPlace.place;
        return query.selectFrom(place)
                .where(place.label.like(Expressions.asString("%").concat(subName).concat("%")))
                .fetch();
    }

    @Override
    public List<Place> getAll() {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QPlace place = QPlace.place;
        return query.selectFrom(place).fetch();
    }
}
