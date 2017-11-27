package sk.liptovzije.service;

import sk.liptovzije.model.DO.EventDO;

public interface IEventService {
    EventDO getById(long id);
    Long saveEvent(EventDO user);
}
