package sk.liptovzije.core.service.event;

import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.event.EventFilter;

import java.util.List;
import java.util.Optional;

public interface IEventService {

    void delete (long id);

    Optional<Event> create(Event event);

    Optional<Event> update (Event event);

    Optional<Event> getById(long id);

    List<Event> getByFilter(EventFilter filter);
}
