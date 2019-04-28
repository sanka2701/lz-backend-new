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
import sk.liptovzije.application.place.Place;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.IFileUrlBuilder;
import sk.liptovzije.core.service.authorization.IAuthorizationService;
import sk.liptovzije.core.service.event.IEventService;
import sk.liptovzije.core.service.file.IFileService;
import sk.liptovzije.core.service.place.IPlaceService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/events")
public class EventsApi {
    private IEventService eventService;
    private IPlaceService placeService;
    private IFileService fileService;
    private IFileUrlBuilder pathBuilder;
    private IAuthorizationService authorizationService;

    @Autowired
    public EventsApi(
            IEventService eventService,
            IPlaceService placeService,
            IFileService fileService,
            IFileUrlBuilder pathBuilder,
            IAuthorizationService authorizationService) {
        this.eventService = eventService;
        this.placeService = placeService;
        this.fileService  = fileService;
        this.pathBuilder  = pathBuilder;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestParam("event") String eventJson,
                                      @RequestParam("place") String placeJson,
                                      @RequestParam("thumbnail") MultipartFile thumbnail,
                                      @RequestParam(value = "fileUrls", required = false) String[] fileUrls,
                                      @RequestParam(value = "files", required = false) MultipartFile[] files,
                                      @AuthenticationPrincipal User user) throws IOException {
        EventParam newEvent = EventParam.fromJson(eventJson);
        PlaceParam newPlace = PlaceParam.fromJson(placeJson);
        List<String> contentFileUrls = fileUrls != null
                ? Arrays.asList(fileUrls)
                : Collections.emptyList();
        List<MultipartFile> urlFiles = files != null
                ? Arrays.asList(files)
                : Collections.emptyList();
        Event event = this.paramToDomain(user, newEvent);
        Place place = newPlace.toDO();

        File thumbnailFile = fileService.save(thumbnail).orElseThrow(InternalError::new);
        List<File> contentFiles = fileService.save(urlFiles);
        Map<String, File> urlMap = pathBuilder.buildFileUrlMap(contentFileUrls, contentFiles);

        long placeId = (
                place.getId() != null
                    ? place
                    : placeService.save(place).orElseThrow(InternalError::new)
                ).getId();
        event.setPlaceId(placeId);
        event.setThumbnail(thumbnailFile);
        event.setFiles(new HashSet<>(contentFiles));
        event.setContent(pathBuilder.replaceUrls(newEvent.getContent(), urlMap));

        return this.eventService.create(event)
                .map(storedEvent -> ResponseEntity.ok(this.eventResponse(storedEvent)))
                .orElseThrow(InternalError::new);
    }

    @PostMapping("/update")
    public ResponseEntity updateEvent(@RequestParam("event") String eventJson,
                                      @RequestParam(value = "place", required = false) String placeJson,
                                      @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                                      @RequestParam(value = "fileUrls", required = false) String[] fileUrls,
                                      @RequestParam(value = "files", required = false) MultipartFile[] files,
                                      @AuthenticationPrincipal User user) throws IOException {
        EventParam eventParam = EventParam.fromJson(eventJson);
        Event event = this.paramToDomain(user, eventParam);
        List<String> contentFileUrls = fileUrls != null
                ? Arrays.asList(fileUrls)
                : Collections.emptyList();
        List<MultipartFile> urlFiles = files != null
                ? Arrays.asList(files)
                : Collections.emptyList();

        if(placeJson != null) {
            PlaceParam placeParam = PlaceParam.fromJson(placeJson);
            Place place = placeParam.toDO();
            event.setPlaceId(
                    place.getId() != null
                            ? place.getId()
                            : placeService.save(place).map(Place::getId).orElseThrow(InternalError::new)
            );
        }

        //todo: delete files which were previously used and are replaced
        if (thumbnail != null) {
            File thumbnailFile = fileService.save(thumbnail).orElseThrow(InternalError::new);
            event.setThumbnail(thumbnailFile);
        }

        if(contentFileUrls.size() > 0) {
            List<File> contentFiles = fileService.save(urlFiles);
            Map<String, File> urlMap = pathBuilder.buildFileUrlMap(contentFileUrls, contentFiles);
            event.setContent(pathBuilder.replaceUrls(eventParam.getContent(), urlMap));
        }

        this.eventService.update(event);
// todo: in order to have application state reflecting the changes this should return updated event object
// return ResponseEntity.ok(this.eventResponse(event));
        return ResponseEntity.noContent().build();
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

    private Event paramToDomain(User owner, EventParam param) {
        return new Event.Builder(owner.getId(), param.getTitle(), param.getContent())
                .id(param.getId())
                .tags(Arrays.stream(param.getTags()).map(TagParam::toDo).collect(Collectors.toSet()))
                .placeId(param.getPlaceId())
                .startDate(param.getStartDate())
                .startTime(param.getStartTime())
                .endDate(param.getEndDate())
                .endTime(param.getEndTime())
                .approved(authorizationService.canApproveEvent(owner))
                .build();
    }

    //todo: transfer just tag ids
    private EventParam domainToParam(Event domainEvent) {
        EventParam param = new EventParam();

        param.setId(domainEvent.getId());
        param.setTags(domainEvent.getTags().stream().map(TagParam::new).toArray(TagParam[]::new));
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
    private TagParam[] tags;
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
