package sk.liptovzije.core.service.place;

import sk.liptovzije.application.place.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlaceService implements IPlaceService {

    private List<Place> placesRepo = new ArrayList<>();


    @Override
    public void save(Place place) {
        this.placesRepo.add(place);
    }

    @Override
    public void update(Place place) {
        this.placesRepo.stream()
                .filter(currentPlace -> currentPlace.getId().equals(place.getId()))
                .forEach(updatedPlace -> updatedPlace = place);
    }

    @Override
    public void delete(long id) {
        this.placesRepo.removeIf(place -> {
            return place.getId().equals(id);
        });
    }

    @Override
    public Optional<Place> getById(long id) {
        return this.placesRepo.stream()
                .filter(place -> place.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Place> getBySubstring(String subName) {
        return this.placesRepo.stream()
                .filter(place -> place.getSearchableName().toLowerCase().contains(subName.toLowerCase()))
                .collect(Collectors.toList());
    }
}
