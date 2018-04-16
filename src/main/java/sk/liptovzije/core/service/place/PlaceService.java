package sk.liptovzije.core.service.place;

import sk.liptovzije.application.place.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PlaceService implements IPlaceService {
    @Override
    public void save(Place place) {

    }

    @Override
    public void update(Place place) {

    }

    @Override
    public Optional<Place> getById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Place> getBySubstring(String substring) {
        return Collections.emptyList();
    }
}
