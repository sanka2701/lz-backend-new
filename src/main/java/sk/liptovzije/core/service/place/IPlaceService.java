package sk.liptovzije.core.service.place;

import sk.liptovzije.application.place.Place;

import java.util.List;
import java.util.Optional;

public interface IPlaceService {
    void update(Place place);

    void delete(long id);

    Optional<Place> save(Place place);

    Optional<Place> getById(long id);

    List<Place> getBySubstring(String substring);

    List<Place> getAll();
}
