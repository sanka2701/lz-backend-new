package sk.liptovzije.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.liptovzije.application.file.File;
import sk.liptovzije.application.photo.WeeklyPhoto;
import sk.liptovzije.application.user.User;
import sk.liptovzije.core.service.IFileUrlBuilder;
import sk.liptovzije.core.service.file.IFileService;
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
    private IFileService fileService;
    private IFileUrlBuilder pathBuilder;

    @Autowired
    public PotwApi (PhotoOfTheWeekService potwService,
                    IFileService fileService,
                    IFileUrlBuilder pathBuilder) {
        this.potwService = potwService;
        this.fileService = fileService;
        this.pathBuilder = pathBuilder;
    }

    @PostMapping
    public ResponseEntity createWeeklyPhoto(@RequestParam("json") String photoJson,
                                            @RequestParam("file") MultipartFile photoFile,
                                            @AuthenticationPrincipal User user) throws IOException {
        WeeklyPhotoParam param = WeeklyPhotoParam.fromJson(photoJson);
        WeeklyPhoto addedPhoto = paramToDomain(param);

        File potwFile = fileService.save(photoFile).orElseThrow(InternalError::new);
        addedPhoto.setPhoto(potwFile);
        addedPhoto.setOwnerId(user.getId());

        return potwService.save(addedPhoto)
                .map(photo -> ResponseEntity.ok(this.photoResponse(photo)))
                .orElseThrow(InternalError::new);
    }

    @PostMapping(path = "/update")
    public ResponseEntity updateWeeklyPhoto(@RequestParam("json") String photoJson,
                                            @RequestParam(name = "file", required = false) MultipartFile photoFile,
                                            @AuthenticationPrincipal User user) throws IOException {
        WeeklyPhotoParam param = WeeklyPhotoParam.fromJson(photoJson);
        WeeklyPhoto updatedPhoto = paramToDomain(param);

        // todo: delete previously used file
        if(photoFile != null) {
            File potwFile = fileService.save(photoFile).orElseThrow(InternalError::new);
            updatedPhoto.setPhoto(potwFile);
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

    private WeeklyPhotoParam domainToParam(WeeklyPhoto photo) {
        WeeklyPhotoParam param = new WeeklyPhotoParam();

        param.setId(photo.getId());
        param.setOwnerId(photo.getOwnerId());
        param.setTitle(photo.getTitle());
        param.setDescription(photo.getDescription());
        param.setPhotoUrl(pathBuilder.toServerUrl(photo.getPhoto().getPath()));
        param.setDateAdded(photo.getDateAdded().toDate().getTime());

        return param;
    }

    private WeeklyPhoto paramToDomain(WeeklyPhotoParam param) {
        WeeklyPhoto photo = new WeeklyPhoto();

        photo.setId(param.getId());
        photo.setOwnerId(param.getOwnerId());
        photo.setTitle(param.getTitle());
        photo.setDescription(param.getDescription());
//        todo: think of an elegant way to set up photo here
//        photo.setPhoto(param.getPhotoUrl());
        photo.setDateAdded(new LocalDate(param.getDateAdded()));

        return photo;
    }

    private Map<String, List> photoResponse(WeeklyPhoto photo) {
        return photoListResponse(Stream.of(photo).collect(Collectors.toList()));
    }

    private Map<String, List> photoListResponse(List<WeeklyPhoto> photos){
        List<WeeklyPhotoParam> params = photos.stream()
                .map(this::domainToParam)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("photos", params);
        }};
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class WeeklyPhotoParam {
    private Long id;
    private Long ownerId;
    private String title;
    private String description;
    private String photoUrl;
    private Long dateAdded;

    static WeeklyPhotoParam fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(json, typeFactory.constructType(WeeklyPhotoParam.class));
    }
}
