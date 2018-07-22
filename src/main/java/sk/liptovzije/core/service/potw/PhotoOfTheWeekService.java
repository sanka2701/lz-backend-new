package sk.liptovzije.core.service.potw;

import sk.liptovzije.application.photo.WeeklyPhoto;

import java.util.List;
import java.util.Optional;

public interface PhotoOfTheWeekService {
    Optional<WeeklyPhoto> save(WeeklyPhoto photo);
    Optional<WeeklyPhoto> update(WeeklyPhoto photo);
    Optional<WeeklyPhoto> getById(long id);
    List<WeeklyPhoto> getAll();
    void delete(long id);
}
