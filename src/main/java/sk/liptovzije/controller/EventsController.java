package sk.liptovzije.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.model.DO.EventDO;
import sk.liptovzije.model.DTO.EventDTO;
import sk.liptovzije.service.IEventService;

@RestController
public class EventsController {

    @Autowired
    private IEventService eventService;

    private static final Logger log = LoggerFactory.getLogger(EventsController.class);

    @RequestMapping(path = "/event/create" , method= RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody EventDTO newEvent){
        log.debug("Creating new event");
        eventService.saveEvent(newEvent.toDo());
        return new ResponseEntity<>("Event Created", HttpStatus.OK);
    }

    @RequestMapping(path = "/event/{id}", method = RequestMethod.GET)
    public ResponseEntity<EventDTO> getSingleEvent(@PathVariable Long id){
        log.debug("Fetching event with id " + id);

        EventDO eventDo = eventService.getById(id);
        EventDTO event = (eventDo != null) ? new EventDTO(eventDo) : null;

        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
