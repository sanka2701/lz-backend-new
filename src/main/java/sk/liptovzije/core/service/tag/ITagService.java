package sk.liptovzije.core.service.tag;


import sk.liptovzije.application.tag.Tag;

import java.util.List;
import java.util.Optional;

public interface ITagService {
    Optional<Tag> save(Tag tag);

    void update(Tag updatedTag);

    void delete(long id);

    Optional<Tag> getById(long id);

    List<Tag> getById(List<Long> ids);

    Optional<Tag> getByLabel(String label);

    List<Tag> getAll();
}
