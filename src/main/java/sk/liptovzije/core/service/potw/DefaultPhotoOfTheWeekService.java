package sk.liptovzije.core.service.potw;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import sk.liptovzije.application.photo.WeeklyPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultPhotoOfTheWeekService implements PhotoOfTheWeekService {
//todo: create sql data table and rewrite using database
    List<WeeklyPhoto> repo;

    public DefaultPhotoOfTheWeekService() {
        this.repo = new ArrayList<>();
    }

    @Override
    public Optional<WeeklyPhoto> save(WeeklyPhoto photo) {
        photo.setId((long)this.repo.size());
        photo.setDateAdded(LocalDate.now());
        this.repo.add(photo);

        return Optional.ofNullable(photo);
    }

    @Override
    public Optional<WeeklyPhoto> update(WeeklyPhoto photo) {
        this.repo.stream()
                .filter(currentPhoto -> currentPhoto.getId().equals(photo.getId()))
                .forEach(updatedPhoto -> updatedPhoto = photo);
        return Optional.of(photo);
    }

    @Override
    public Optional<WeeklyPhoto> getById(long id) {
        return this.repo.stream()
                .filter(photo -> photo.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<WeeklyPhoto> getAll() {
        return repo;
    }

    @Override
    public void delete(long id) {
        this.repo.removeIf(photo -> photo.getId().equals(id));
    }
}
