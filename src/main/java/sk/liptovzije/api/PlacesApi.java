package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.application.place.Place;
import sk.liptovzije.core.service.place.IPlaceService;

import javax.validation.Valid;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/places")
public class PlacesApi {

    private IPlaceService placeService;

    @Autowired
    public  PlacesApi(IPlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/id")
    public ResponseEntity getPlaceById(@Valid @RequestBody long id) {
//        Map result = this.placeService.getById(id)
//                .map(this::singlePlaceResponse)
//                .orElse(Collections.emptyMap());
//        return ResponseEntity.ok(result);

        Place requestedPlace = this.placeService.getById(id).get();
        return ResponseEntity.ok(singlePlaceResponse(requestedPlace));
    }

    @GetMapping("/name")
    public ResponseEntity listPlacesByName(@Valid @RequestBody String subname, BindingResult bindingResult) {
        List<Place> listedPlaces = this.placeService.getBySubstring(subname);
        return ResponseEntity.ok(placeListResponse(listedPlaces));
    }

    @PostMapping
    public ResponseEntity createPlace(@Valid @RequestBody PlaceParam newPlace, BindingResult bindingResult) {
        Place place = new Place(newPlace.name, newPlace.longitude, newPlace.latitude);
        this.placeService.save(place);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deletePlace(@Valid @RequestBody long id, BindingResult bindingResult) {
        this.placeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/update", method = POST)
    public ResponseEntity updatePlace(@Valid @RequestBody PlaceParam newPlace, BindingResult bindingResult) {
        return null;
    }

    private Map<String, Object> singlePlaceResponse(Place place){
        return new HashMap<String, Object>() {{
            put("place", place);
        }};
    }

    private Map<String, List> placeListResponse(List<Place> places){
        return new HashMap<String, List>() {{
            put("places", places);
        }};
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class PlaceParam {
        @NotBlank(message = "can't be empty")
        private String name;
        @NotBlank(message = "can't be empty")
        private Double longitude;
        @NotBlank(message = "can't be empty")
        private double latitude;
    }
}
