package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.core.service.place.IPlaceService;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/places")
public class PlacesApi {

    private IPlaceService placeService;

    @Autowired
    public  PlacesApi(IPlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity listPlaces(@Valid @RequestBody GetPlaceParam filter, BindingResult bindingResult) {
        return null;
    }

    @PostMapping
    public ResponseEntity createPlace(@Valid @RequestBody PlaceParam newPlace, BindingResult bindingResult) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity deletePlace(@Valid @RequestBody PlaceParam newPlace, BindingResult bindingResult) {
        return null;
    }

    @RequestMapping(path = "/update", method = POST)
    public ResponseEntity updatePlace(@Valid @RequestBody PlaceParam newPlace, BindingResult bindingResult) {
        return null;
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPlaceParam {
        private Long id;
        private String subName;
    }
}
