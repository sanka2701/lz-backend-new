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
    private IFileService fileService;
    private IFileUrlBuilder pathBuilder;
    private IAuthorizationService authorizationService;

    @Autowired
    public EventsApi(
            ITagService tagService,
            IEventService eventService,
            IFileService fileService,
            IFileUrlBuilder pathBuilder,
            IAuthorizationService authorizationService) {
        this.tagService   = tagService;
        this.eventService = eventService;
        this.fileService  = fileService;
        this.pathBuilder  = pathBuilder;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestParam("event") String eventJson,
                                      @RequestParam("thumbnail") MultipartFile thumbnail,
                                      @RequestParam(value = "fileUrls", required = false) String[] fileUrls,
                                      @RequestParam(value = "files", required = false) MultipartFile[] files,
                                      @AuthenticationPrincipal User user) throws IOException {
        Event event = resolveEventDependencies(eventJson, thumbnail, fileUrls, files, user);

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
        Event updatedEvent = resolveEventDependencies(eventJson, thumbnail, fileUrls, files, user);
        Event event = eventService.getById(updatedEvent.getId()).orElseThrow(ResourceNotFoundException::new);

        if(updatedEvent.getThumbnail() != null) {
            event.setThumbnail(updatedEvent.getThumbnail());
        }
        event.setPlaceId(updatedEvent.getPlaceId());
        event.setTags(updatedEvent.getTags());
        event.setTitle(updatedEvent.getTitle());
        event.setContent(updatedEvent.getContent());
        event.setStartDate(updatedEvent.getStartDate());
        event.setStartTime(updatedEvent.getStartTime());
        event.setEndDate(updatedEvent.getEndDate());
        event.setEndTime(updatedEvent.getEndTime());
        event.setApproved(updatedEvent.getApproved());

        this.eventService.update(event);

        // todo: delete unused files after update
        return ResponseEntity.ok(eventResponse(event));
    }

    @GetMapping()
    public ResponseEntity getEvent(@RequestParam("id") long id) {
        return this.eventService.getById(id)
                .map(event -> ResponseEntity.ok(this.eventResponse(event)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/all")
    public ResponseEntity filterEvents() {
        List<Event> events = this.eventService.getAll();
        return ResponseEntity.ok(eventListResponse(events));
    }

    private Event resolveEventDependencies(String eventJson,
                                           MultipartFile thumbnail,
                                           String[] fileUrls,
                                           MultipartFile[] files,
                                           User user) throws IOException {
        EventParam eventParam = EventParam.fromJson(eventJson);
        Event event = this.paramToDomain(user, eventParam);

        if (thumbnail != null) {
            File thumbnailFile = fileService.save(thumbnail).orElseThrow(InternalError::new);
            event.setThumbnail(thumbnailFile);
        }

        if(fileUrls != null && files != null) {
            List<String> contentFileUrls = Arrays.asList(fileUrls);
            List<MultipartFile> urlFiles = Arrays.asList(files);
            List<File> contentFiles = fileService.save(urlFiles);
            Map<String, File> urlMap = pathBuilder.buildFileUrlMap(contentFileUrls, contentFiles);
            event.setContent(pathBuilder.replaceUrls(eventParam.getContent(), urlMap));
        }

        return event;
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
