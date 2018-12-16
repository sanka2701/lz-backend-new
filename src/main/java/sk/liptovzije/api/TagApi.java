package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.liptovzije.application.tag.Tag;
import sk.liptovzije.core.service.tag.ITagService;
import sk.liptovzije.utils.exception.InvalidRequestException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/eventtag")
public class TagApi {
    private ITagService tagService;

    @Autowired
    public TagApi(ITagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity createTag(@Valid @RequestBody TagParam tagParam, BindingResult bindingResult) {
        checkInput(tagParam, bindingResult);
        Tag tag = tagParam.toDo();
        Map response = tagService.save(tag)
                .map(this::tagResponse)
                .orElseThrow(InternalError::new);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity updateTag(@Valid @RequestBody TagParam tagParam, BindingResult bindingResult) {
        checkInput(tagParam, bindingResult);
        Tag tag = tagParam.toDo();
        tagService.update(tag);
        return ResponseEntity.ok(tagResponse(tag));
    }

    @GetMapping("/id")
    public ResponseEntity getTagById(@RequestParam("id") long id) {
        Tag requestedTag = this.tagService.getById(id).get();
        return ResponseEntity.ok(tagResponse(requestedTag));
    }

    @GetMapping
    public ResponseEntity listTags() {
        List<Tag> userList = tagService.getAll();
        return ResponseEntity.ok(this.tagResponse(userList));
    }

    @DeleteMapping
    public ResponseEntity deleteTag(@RequestParam("id") long id) {
        this.tagService.delete(id);
        return ResponseEntity.ok().build();
    }

    private void checkInput(@Valid @RequestBody TagParam tagParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        if (tagService.getByLabel(tagParam.getLabel()).isPresent()) {
            bindingResult.rejectValue("label", "DUPLICATED", "err.duplicatedTag");
        }

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
    }

    private Map<String, List> tagResponse(Tag tag){
        return tagResponse(Stream.of(tag).collect(Collectors.toList()));
    }

    private Map<String, List> tagResponse(List<Tag> tags) {
        List<TagParam> params = tags.stream()
                .map(TagParam::new)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("tags", params);
        }};
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class TagParam {
    private Long id;
    @NotBlank(message = "can't be empty")
    private String label;

    public TagParam(Tag domainObject) {
        this.id = domainObject.getId();
        this.label = domainObject.getLabel();
    }

    public Tag toDo() {
        Tag tag = new Tag();
        tag.setId(this.id);
        tag.setLabel(this.label);
        return tag;
    }
}