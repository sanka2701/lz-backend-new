package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.photo.WeeklyPhoto;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.FileUrlBuilder;
import sk.liptovzije.core.service.storage.IStorageService;
import sk.liptovzije.core.service.potw.PhotoOfTheWeekService;
import sk.liptovzije.utils.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(path = "/potw")
public class PotwApi {

    private PhotoOfTheWeekService potwService;
    private IStorageService storageService;
    private FileUrlBuilder pathBuilder;

    @Autowired
    public PotwApi (PhotoOfTheWeekService potwService,
                    IStorageService storageService,
                    FileUrlBuilder pathBuilder) {
        this.potwService = potwService;
        this.storageService = storageService;
        this.pathBuilder = pathBuilder;
    }

    @PostMapping
    public ResponseEntity createWeeklyPhoto(@RequestParam("json") String photoJson,
                                            @RequestParam("storage") MultipartFile photoFile,
                                            @AuthenticationPrincipal User user) throws IOException {
        WeeklyPhotoParam param = WeeklyPhotoParam.fromJson(photoJson);
        WeeklyPhoto addedPhoto = paramToWeeklyPhoto(param);

        addedPhoto.setOwnerId(user.getId());
        addedPhoto.setPhotoUrl(pathBuilder.toServerUrl(storageService.store(photoFile)));
        return potwService.save(addedPhoto)
                .map(photo -> ResponseEntity.ok(this.photoResponse(photo)))
                .orElseThrow(InternalError::new);
    }

    @PostMapping(path = "/update")
    public ResponseEntity updateWeeklyPhoto(@RequestParam("json") String photoJson,
                                            @RequestParam(name = "storage", required = false) MultipartFile photoFile,
                                            @AuthenticationPrincipal User user) throws IOException {
        WeeklyPhotoParam param = WeeklyPhotoParam.fromJson(photoJson);
        WeeklyPhoto updatedPhoto = paramToWeeklyPhoto(param);

        if(photoFile != null) {
            updatedPhoto.setPhotoUrl(pathBuilder.toServerUrl(storageService.store(photoFile)));
        }
        return potwService.update(updatedPhoto)
                .map(photo -> ResponseEntity.ok(this.photoResponse(photo)))
                .orElseThrow(InternalError::new);
    }

    @DeleteMapping
    public ResponseEntity deleteWeeklyPhoto(@RequestParam("id") Long id) {
        this.potwService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity getPhoto(@RequestParam("id") long id) {
        return this.potwService.getById(id)
                .map(photo -> ResponseEntity.ok(this.photoResponse(photo)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping(path = "/list")
    public ResponseEntity listPhotos() {
        List<WeeklyPhoto> photos = this.potwService.getAll();
        return ResponseEntity.ok(this.photoListResponse(photos));
    }

    private WeeklyPhoto paramToWeeklyPhoto(WeeklyPhotoParam param) {
        return new WeeklyPhoto(
                param.getId(),
                param.getOwnerId(),
                param.getTitle(),
                param.getDescription(),
                param.getPhotoUrl(),
                new LocalDate(param.getDateAdded())
        );
    }

    private Map<String, List> photoResponse(WeeklyPhoto photo) {
        return photoListResponse(Stream.of(photo).collect(Collectors.toList()));
    }

    private Map<String, List> photoListResponse(List<WeeklyPhoto> photos){
        List<WeeklyPhotoParam> params = photos.stream()
                .map(WeeklyPhotoParam::new)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("photos", params);
        }};
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class WeeklyPhotoParam {
    private Long id;
    private Long ownerId;
    private String title;
    private String description;
    private String photoUrl;
    private Long dateAdded;

    WeeklyPhotoParam(WeeklyPhoto photo) {
        this.id = photo.getId();
        this.ownerId = photo.getOwnerId();
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.photoUrl = photo.getPhotoUrl();
        this.dateAdded = photo.getDateAdded().toDate().getTime();
    }

    static WeeklyPhotoParam fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(json, typeFactory.constructType(WeeklyPhotoParam.class));
    }
}
