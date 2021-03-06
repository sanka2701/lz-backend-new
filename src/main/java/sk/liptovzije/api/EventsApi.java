package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.file.File;
import sk.liptovzije.application.tag.Tag;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.IFileUrlBuilder;
import sk.liptovzije.core.service.authorization.IAuthorizationService;
import sk.liptovzije.core.service.event.IEventService;
import sk.liptovzije.core.service.file.IFileService;
import sk.liptovzije.core.service.post.IPostService;
import sk.liptovzije.core.service.tag.ITagService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/events")
public class EventsApi {
    private ITagService tagService;
    private IEventService eventService;
    private IPostService postService;
    private IFileUrlBuilder pathBuilder;
    private IAuthorizationService authorizationService;

    @Autowired
    public EventsApi(
            ITagService tagService,
            IEventService eventService,
            IPostService postService,
            IFileUrlBuilder pathBuilder,
            IAuthorizationService authorizationService) {
        this.tagService   = tagService;
        this.eventService = eventService;
        this.postService  = postService;
        this.pathBuilder  = pathBuilder;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestParam("event") String eventJson,
                                      @RequestParam("thumbnail") MultipartFile thumbnail,
                                      @RequestParam(value = "fileUrls", required = false) String[] fileUrls,
                                      @RequestParam(value = "files", required = false) MultipartFile[] files,
                                      @AuthenticationPrincipal User user) throws IOException {
        EventParam eventParam = EventParam.fromJson(eventJson);
        Event event = this.paramToDomain(user, eventParam);
        postService.resolveFileDependencies(event, thumbnail, fileUrls, files);

        return this.eventService.create(event)
                .map(storedEvent -> ResponseEntity.ok(this.eventResponse(storedEvent)))
                .orElseThrow(InternalError::new);
    }

    @PostMapping("/update")
    public ResponseEntity updateEvent(@RequestParam("event") String eventJson,
                                      @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                                      @RequestParam(value = "fileUrls", required = false) String[] fileUrls,
                                      @RequestParam(value = "files", required = false) MultipartFile[] files,
                                      @AuthenticationPrincipal User user) throws IOException {
        EventParam eventParam = EventParam.fromJson(eventJson);
        Event updatedEvent  = this.paramToDomain(user, eventParam);
        Event originalEvent = eventService.getById(updatedEvent.getId()).orElseThrow(ResourceNotFoundException::new);

        postService.resolveFileDependencies(updatedEvent, thumbnail, fileUrls, files);
        Set<File> toBeDeleted =
                this.postService.updateContentFiles(originalEvent, updatedEvent);

        originalEvent.setPlaceId(updatedEvent.getPlaceId());
        originalEvent.setTags(updatedEvent.getTags());
        originalEvent.setTitle(updatedEvent.getTitle());
        originalEvent.setContent(updatedEvent.getContent());
        originalEvent.setStartDate(updatedEvent.getStartDate());
        originalEvent.setStartTime(updatedEvent.getStartTime());
        originalEvent.setEndDate(updatedEvent.getEndDate());
        originalEvent.setEndTime(updatedEvent.getEndTime());
        originalEvent.setApproved(updatedEvent.getApproved());

        eventService.update(originalEvent);
        postService.removeUnusedFiles(toBeDeleted);

        return ResponseEntity.ok(eventResponse(originalEvent));
    }

    @GetMapping()
    public ResponseEntity getEventById(@RequestParam("id") long id) {
        return this.eventService.getById(id)
                .map(event -> ResponseEntity.ok(this.eventResponse(event)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/all")
    public ResponseEntity getAllEvents() {
        List<Event> events = this.eventService.getAll();
        return ResponseEntity.ok(eventListResponse(events));
    }

    private Event paramToDomain(User owner, EventParam param) {
        List<Long> domainTags =  Arrays.stream(param.getTags()).collect(Collectors.toList());
        Set tags = new HashSet<>(this.tagService.getById(domainTags));

        return new Event.Builder(owner.getId(), param.getTitle(), param.getContent())
                .id(param.getId())
                .tags(tags)
                .placeId(param.getPlaceId())
                .startDate(param.getStartDate())
                .startTime(param.getStartTime())
                .endDate(param.getEndDate())
                .endTime(param.getEndTime())
                .approved(authorizationService.canApproveEvent(owner))
                .build();
    }

    private EventParam domainToParam(Event domainEvent) {
        EventParam param = new EventParam();

        param.setId(domainEvent.getId());
        param.setTags(domainEvent.getTags().stream().map(Tag::getId).toArray(Long[]::new));
        param.setPlaceId(domainEvent.getPlaceId());
        param.setOwnerId(domainEvent.getOwnerId());
        param.setTitle(domainEvent.getTitle());
        param.setContent(domainEvent.getContent());
        param.setThumbnail(pathBuilder.toServerUrl(domainEvent.getThumbnail().getPath()));
        param.setStartDate(domainEvent.getStartDate().toDate().getTime());
        param.setEndDate(domainEvent.getEndDate().toDate().getTime());
        param.setStartTime((long) domainEvent.getStartTime().getMillisOfDay());
        param.setEndTime((long) domainEvent.getEndTime().getMillisOfDay());
        param.setApproved(domainEvent.getApproved());

        return param;
    }

    private Map<String, List> eventResponse(Event event) {
        return eventListResponse(Stream.of(event).collect(Collectors.toList()));
    }

    private Map<String, List> eventListResponse(List<Event> events) {
        List<EventParam> params = events.stream()
                .map(this::domainToParam)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("events", params);
        }};
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class EventParam {
    private Long id;
    private Long placeId;
    private Long ownerId;
    private Long[] tags;
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

    static EventParam fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(json, typeFactory.constructType(EventParam.class));
    }
}
