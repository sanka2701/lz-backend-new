package sk.liptovzije.core.service.place;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.place.Place;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class PlaceService implements IPlaceService {

    private List<Place> placesRepo = new ArrayList<>();
    private int FUZZY_SCORE_TRESHOLD = 85;

    public PlaceService() {
        placesRepo.add(new Place(0L, null,"Ahoj", "Addressa 1", 19.596746688751296, 49.09751301668884));
        placesRepo.add(new Place(1L, null,"Ahojky", "Addressa 2", 19.584112410359808, 49.08447298057577));
        placesRepo.add(new Place(2L, null, "Nazdar", "Addressa 3", 19.560835126597226, 49.093241657941725));
    }

    @Override
    public Optional<Place> save(Place place) {
        this.placesRepo.add(place);

        return Optional.ofNullable(place);
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
                .filter(place -> {
                    String normalizedName  = StringUtils.stripAccents(place.getLabel()).toLowerCase();
                    String normalizedQuery = StringUtils.stripAccents(subName).toLowerCase();
                    return (normalizedName.contains(normalizedQuery)
                            || FuzzySearch.ratio(normalizedName, normalizedQuery) > FUZZY_SCORE_TRESHOLD);
                })
                .collect(Collectors.toList());
    }
}
