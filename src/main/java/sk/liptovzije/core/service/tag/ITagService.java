package sk.liptovzije.core.service.tag;


import sk.liptovzije.application.tag.EventTag;

import java.util.List;
import java.util.Optional;

public interface ITagService {
    Optional<EventTag> save(EventTag tag);

    void update(EventTag updatedTag);

    void delete(long id);

    Optional<EventTag> getById(long id);

    Optional<EventTag> getByLabel(String label);

    List<EventTag> getAll();
}
