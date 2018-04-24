package sk.liptovzije.core.service.event;

import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;

import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {
    @Override
    public Optional<Event> save(Event event) {

        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Optional<Event> update(Event event) {
        return null;
    }

    @Override
    public Optional<Event> getById(long id) {
        return null;
    }

    @Override
    public List<Event> getByFilter() {
        return null;
    }
}
