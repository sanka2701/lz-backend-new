package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.event.EventFilter;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.event.IEventService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/events")
public class EventsApi {

    private IEventService eventService;

    @Autowired
    public EventsApi(IEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity createEvent(@Valid @RequestBody NewEventParam newEvent, @AuthenticationPrincipal User user) {
        Event event = this.paramToEvent(user.getId(), newEvent);

        return this.eventService.save(event)
                .map(storedEvent -> ResponseEntity.ok(this.eventResponse(event)))
                .orElseThrow(InternalError::new);
    }

    @PostMapping("/update")
    public ResponseEntity updateEvent(@Valid @RequestBody NewEventParam updatedEvent) {
        Event event = this.paramToEvent(updatedEvent.getOwnerId(), updatedEvent);

        return this.eventService.update(event)
                .map(storedEvent -> ResponseEntity.ok(this.eventResponse(event)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping()
    public ResponseEntity getEvent(@Valid @RequestBody long id) {
        return this.eventService.getById(id)
                .map(event -> ResponseEntity.ok(this.eventResponse(event)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/filter")
    public ResponseEntity filterEvents(@Valid @RequestBody EventFilter filter) {
        // todo
        return ResponseEntity.ok().build();
    }

    private Event paramToEvent(long ownerId, NewEventParam param) {
        return new Event.Builder(ownerId, param.getHeading(), param.getContent())
                .startDate(param.getStartDate())
                .startTime(param.getStartTime())
                .endDate(param.getEndDate())
                .endTime(param.getEndTime())
                .thumbnail(param.getThumbnail())
                .build();
    }

    private Map<String, Object> eventResponse(Event event) {
        return new HashMap<String, Object>() {{
            put("event", event);
        }};
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class NewEventParam {
    private Long placeId;
    private Long ownerId;
    @NotBlank(message = "can't be empty")
    private String heading;
    @NotBlank(message = "can't be empty")
    private String content;
    private String thumbnail;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
}
