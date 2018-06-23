package sk.liptovzije.core.service.event;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {

    private List<Event> eventRepo = new ArrayList<>();

    private String evntContent= "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p> <blockquote> <p>Nemo enim ipsam voluptatem quia voluptas sit&nbsp;</p> </blockquote> <figure class=\"easyimage easyimage-full\"><img alt=\"\" src=\"https://vignette.wikia.nocookie.net/totally-accurate-battle-simulator/images/e/e7/Owl.jpg/revision/latest?cb=20170329054704\" width=\"274\" /><figcaption></figcaption></figure><p>Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?</p>";

    public EventService() {
        Event event1 = new Event.Builder(0, "Pltkovanie", evntContent)
                .startDate(new LocalDate(1529749495911L))
                .startTime(new LocalTime(3600000L))
                .endDate(new LocalDate(1530267895911L))
                .endTime(new LocalTime(64800000L))
                .placeId(0)
                .thumbnail("http://www.liptovzije.sk/wp-content/uploads/2018/06/mjf18-1-1024x724.png")
                .build();

        Event event2 = new Event.Builder(0, "Varkanie vsetkoho fajneho", evntContent)
                .startDate(new LocalDate(1529749495911L))
                .startTime(new LocalTime(3600000L))
                .endDate(new LocalDate(1530267895911L))
                .endTime(new LocalTime(64800000L))
                .placeId(1)
                .thumbnail("http://www.liptovzije.sk/wp-content/uploads/2018/06/polievkovy-festival-1024x576.jpg")
                .build();

        eventRepo.add(event1);
        eventRepo.add(event2);
    }

    @Override
    public Optional<Event> save(Event event) {
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
    public List<Event> getByFilter() {
        return this.eventRepo;
    }
}
