package sk.liptovzije.core.service.event;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.event.EventFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {

    private List<Event> eventRepo = new ArrayList<>();
    private int FUZZY_SCORE_TRESHOLD = 85;

    private String evntContent= "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p> <blockquote> <p>Nemo enim ipsam voluptatem quia voluptas sit&nbsp;</p> </blockquote> <figure class=\"easyimage easyimage-full\"><img alt=\"\" src=\"https://vignette.wikia.nocookie.net/totally-accurate-battle-simulator/images/e/e7/Owl.jpg/revision/latest?cb=20170329054704\" width=\"274\" /><figcaption></figcaption></figure><p>Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?</p>";

    public EventService() {
        Event event1 = new Event.Builder(0, "Pltkovanie", evntContent)
                .startDate(new LocalDate(1529749495911L))
                .startTime(new LocalTime(3600000L))
                .endDate(new LocalDate(1530267895911L))
                .endTime(new LocalTime(64800000L))
                .placeId(0L)
                .thumbnail("http://localhost:8080/img/2018/6/17/b5d717eb5f994b4a804522249f2d17c1.jpg")
                .approved(true)
                .build();

        Event event2 = new Event.Builder(0, "Varkanie vsetkoho fajneho", evntContent)
                .startDate(new LocalDate(1529749495911L))
                .startTime(new LocalTime(3600000L))
                .endDate(new LocalDate(1530267895911L))
                .endTime(new LocalTime(64800000L))
                .placeId(1L)
                .thumbnail("http://localhost:8080/img/2018/6/17/2067fa930572479eb3463e9bde7a5f51.jpg")
                .approved(false)
                .build();

        Event event3 = new Event.Builder(0, "Varkanie vsetkoho fajneho", evntContent)
                .startDate(new LocalDate(1529749495911L))
                .startTime(new LocalTime(3600000L))
                .endDate(new LocalDate(1530267895911L))
                .endTime(new LocalTime(64800000L))
                .placeId(1L)
                .thumbnail("http://localhost:8080/img/2018/6/17/4679cc8fb82945d9ba69c8258c454222.jpg")
                .approved(true)
                .build();

        eventRepo.add(event1);
        eventRepo.add(event2);
        eventRepo.add(event3);

//        for (int i =0; i < 1000; i++) {
//            eventRepo.add(
//                    new Event.Builder(0, "Pltkovanie", evntContent)
//                    .startDate(new LocalDate(1529749495911L))
//                    .startTime(new LocalTime(3600000L))
//                    .endDate(new LocalDate(1530267895911L))
//                    .endTime(new LocalTime(64800000L))
//                    .placeId(0)
//                    .thumbnail("http://www.liptovzije.sk/wp-content/uploads/2018/06/mjf18-1-1024x724.png")
//                    .approved(true)
//                    .build()
//            );
//        }
    }

    @Override
    public Optional<Event> create(Event event) {
        this.eventRepo.add(event);

        return Optional.ofNullable(event);
    }

    @Override
    public void delete(long id) {
        this.eventRepo.removeIf(event -> event.getId().equals(id));
    }

    @Override
    public Optional<Event> update(Event event) {
        this.eventRepo.stream()
                .filter(currentEvent -> currentEvent.getId().equals(event.getId()))
                .forEach(updatedEvent -> updatedEvent = event);

        return Optional.ofNullable(event);
    }

    @Override
    public Optional<Event> getById(long id) {
        return this.eventRepo.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Event> getByFilter(EventFilter filter) {
        return this.eventRepo.stream()
                .filter(event -> {
                    boolean fitsFilter = true;

                    if (filter.getOwnerId() != null) {
                        fitsFilter = fitsFilter && event.getOwnerId().equals(filter.getOwnerId());
                    }

                    if (filter.getTitle() != null) {
                        String normalizedName  = StringUtils.stripAccents(event.getTitle()).toLowerCase();
                        String normalizedQuery = StringUtils.stripAccents(filter.getTitle()).toLowerCase();
                        fitsFilter = fitsFilter && (normalizedName.contains(normalizedQuery)
                                || FuzzySearch.ratio(normalizedName, normalizedQuery) > FUZZY_SCORE_TRESHOLD);
                    }

                    if(filter.getStartDate() != null) {
                        fitsFilter = fitsFilter &&
                                (event.getStartDate().isAfter(filter.getStartDate()) ||
                                 event.getStartDate().equals(filter.getStartDate()));
                    }

                    if(filter.getStartTime() != null) {
                        fitsFilter = fitsFilter &&
                                (event.getStartTime().isAfter(filter.getStartTime()) ||
                                 event.getStartTime().equals(filter.getStartTime()));
                    }

                    if(filter.getEndDate() != null) {
                        fitsFilter = fitsFilter &&
                                (event.getStartDate().isBefore(filter.getEndDate()) ||
                                 event.getStartDate().equals(filter.getEndDate()));
                    }

                    //todo might not make sense to filter according to end time
                    if(filter.getEndTime() != null) {
                        fitsFilter = fitsFilter &&
                                (event.getStartTime().isBefore(filter.getEndTime()) ||
                                 event.getStartTime().equals(filter.getEndTime()));
                    }

                    if (filter.getApproved() != null) {
                        fitsFilter = fitsFilter && event.getApproved().equals(filter.getApproved());
                    }

                    return fitsFilter;
                })
                .collect(Collectors.toList());
    }
}
