package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.event.EventFilter;
import sk.liptovzije.application.place.Place;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.FileUrlBuilder;
import sk.liptovzije.core.service.authorization.IAuthorizationService;
import sk.liptovzije.core.service.event.IEventService;
import sk.liptovzije.core.service.file.IStorageService;
import sk.liptovzije.core.service.place.IPlaceService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/events")
public class EventsApi {

    private IEventService eventService;
    private IPlaceService placeService;
    private IStorageService storageService;
    private IAuthorizationService authorizationService;
    private FileUrlBuilder pathBuilder;

    @Autowired
    public EventsApi(
            IEventService eventService,
            IPlaceService placeService,
            IStorageService storageService,
            IAuthorizationService authorizationService,
            FileUrlBuilder pathBuilder){
        this.eventService = eventService;
        this.placeService = placeService;
        this.storageService = storageService;
        this.authorizationService = authorizationService;
        this.pathBuilder = pathBuilder;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestParam("event") String eventJson,
                                      @RequestParam("place") String placeJson,
                                      @RequestParam("thumbnail") MultipartFile thumbnail,
                                      @RequestParam("fileUrls") String[] contentFileUrls,
                                      @RequestParam("file") MultipartFile[] files,
                                      @AuthenticationPrincipal User user
    ) throws IOException {
        EventParam newEvent = EventParam.fromJson(eventJson);
        PlaceParam newPlace = PlaceParam.fromJson(placeJson);
        Event event = this.paramToEvent(user, newEvent);
        Place place = newPlace.toDO();

        if(contentFileUrls.length != files.length) {
            throw new IllegalArgumentException("List of files and corresponding urls has to be equal size");
        }

        Map<String, String> urlMap = new HashMap<>();
        for(int i = 0; i < files.length; i++) {
            urlMap.put(contentFileUrls[i], pathBuilder.toServerUrl(storageService.store(files[i])));
        }

        long placeId = place.getId() != null ? place.getId() : placeService.save(place)
                .orElseThrow(InternalError::new)
                .getId();
        event.setPlaceId(placeId);
        event.setContent(pathBuilder.replaceUrls(newEvent.getContent(), urlMap));
        event.setThumbnail(pathBuilder.toServerUrl(storageService.store(thumbnail)));

        return this.eventService.create(event)
                .map(storedEvent -> ResponseEntity.ok(this.eventResponse(storedEvent)))
                .orElseThrow(InternalError::new);
    }

    @PostMapping("/update")
    public ResponseEntity updateEvent(@Valid @RequestBody EventParam updatedEvent,
                                      @AuthenticationPrincipal User user) {
        Event event = this.paramToEvent(user, updatedEvent);

        return this.eventService.update(event)
                .map(storedEvent -> ResponseEntity.ok(this.eventResponse(event)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping("/approve")
    public ResponseEntity approveEvent(@RequestParam("id") long id) {
        this.eventService.approve(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity getEvent(@RequestParam("id") long id) {
        return this.eventService.getById(id)
                .map(event -> ResponseEntity.ok(this.eventResponse(event)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping("/filter")
    public ResponseEntity filterEvents(@Valid @RequestBody EventFilter filter) {
        List<Event> events = this.eventService.getByFilter(filter);
        return ResponseEntity.ok(eventListResponse(events));
    }

    private Event paramToEvent(User owner, EventParam param) {
        return new Event.Builder(owner.getId(), param.getTitle(), param.getContent())
                .placeId(param.getPlaceId())
                .startDate(param.getStartDate())
                .startTime(param.getStartTime())
                .endDate(param.getEndDate())
                .endTime(param.getEndTime())
                .thumbnail(param.getThumbnail())
                .approved(authorizationService.canApproveEvent(owner))
                .build();
    }

    private Map<String, List> eventResponse(Event event) {
        return eventListResponse(Stream.of(event).collect(Collectors.toList()));
    }

    private Map<String, List> eventListResponse(List<Event> events){
        List<EventParam> params = events.stream()
                .map(EventParam::new)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("events", params);
        }};
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class EventParam {
    private Long id;
    private Long placeId;
    private Long ownerId;
    @NotBlank(message = "can't be empty")
    private String title;
    @NotBlank(message = "can't be empty")
    private String content;
    private String thumbnail;
    private Long startDate;
    private Long startTime;
    private Long endDate;
    private Long endTime;
    private Boolean approved;

    public static EventParam fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(json, typeFactory.constructType(EventParam.class));
    }

    public EventParam(Event domainEvent) {
        this.id        = domainEvent.getId();
        this.placeId   = domainEvent.getPlaceId();
        this.ownerId   = domainEvent.getOwnerId();
        this.title     = domainEvent.getTitle();
        this.content   = domainEvent.getContent();
        this.thumbnail = domainEvent.getThumbnail();
        this.startDate = domainEvent.getStartDate().toDate().getTime();
        this.endDate   = domainEvent.getEndDate().toDate().getTime();
        this.startTime = (long) domainEvent.getStartTime().getMillisOfDay();
        this.endTime   = (long) domainEvent.getEndTime().getMillisOfDay();
        this.approved  = domainEvent.getApproved();
    }
}
