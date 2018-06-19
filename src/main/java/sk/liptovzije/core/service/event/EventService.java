package sk.liptovzije.core.service.event;

import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {

    private List<Event> eventRepo = new ArrayList<>();

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
        return null;
    }
}
