package sk.liptovzije.core.service.event;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.event.EventFilter;
import sk.liptovzije.application.event.QEvent;
import sk.liptovzije.application.tag.QTag;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {
    @PersistenceContext
    private EntityManager entityManager;

//    private List<Event> eventRepo = new ArrayList<>();
//    private int FUZZY_SCORE_TRESHOLD = 85;
//    private String evntContent= "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p> <blockquote> <p>Nemo enim ipsam voluptatem quia voluptas sit&nbsp;</p> </blockquote> <figure class=\"easyimage easyimage-full\"><img alt=\"\" src=\"https://vignette.wikia.nocookie.net/totally-accurate-battle-simulator/images/e/e7/Owl.jpg/revision/latest?cb=20170329054704\" width=\"274\" /><figcaption></figcaption></figure><p>Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?</p>";
//    public EventService() {
//        Event event1 = new Event.Builder(0, "Pltkovanie", evntContent)
//                .startDate(new LocalDate(1529749495911L))
//                .startTime(new LocalTime(3600000L))
//                .endDate(new LocalDate(1530267895911L))
//                .endTime(new LocalTime(64800000L))
//                .placeId(0L)
//                .thumbnail("http://localhost:8080/img/2018/6/17/b5d717eb5f994b4a804522249f2d17c1.jpg")
//                .approved(true)
//                .build();
//
//        Event event2 = new Event.Builder(0, "Varkanie vsetkoho fajneho", evntContent)
//                .startDate(new LocalDate(1529749495911L))
//                .startTime(new LocalTime(3600000L))
//                .endDate(new LocalDate(1530267895911L))
//                .endTime(new LocalTime(64800000L))
//                .placeId(1L)
//                .thumbnail("http://localhost:8080/img/2018/6/17/2067fa930572479eb3463e9bde7a5f51.jpg")
//                .approved(false)
//                .build();
//
//        Event event3 = new Event.Builder(0, "Varkanie vsetkoho fajneho", evntContent)
//                .startDate(new LocalDate(1529749495911L))
//                .startTime(new LocalTime(3600000L))
//                .endDate(new LocalDate(1530267895911L))
//                .endTime(new LocalTime(64800000L))
//                .placeId(1L)
//                .thumbnail("http://localhost:8080/img/2018/6/17/4679cc8fb82945d9ba69c8258c454222.jpg")
//                .approved(true)
//                .build();
//
//        eventRepo.add(event1);
//        eventRepo.add(event2);
//        eventRepo.add(event3);
//
//    }

    @Override
    public Optional<Event> create(Event event) {
        try {
            entityManager.unwrap(Session.class).save(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(event);
    }

    @Override
    public void delete(long id) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEvent event = QEvent.event;
        query.delete(event).where(event.id.eq(id)).execute();
    }

    @Override
    public Optional<Event> update(Event updatedEvent) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEvent event = QEvent.event;
        query.update(event).where(event.id.eq(updatedEvent.getId())).execute();

        return Optional.ofNullable(updatedEvent);
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
    public List<Event> getByFilter(EventFilter filter) {
        HibernateQueryFactory query = new HibernateQueryFactory(entityManager.unwrap(Session.class));
        QEvent event = QEvent.event;
        List<Event> result = query.selectFrom(event).fetch();

        return result;
    }
}
