package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.liptovzije.application.tag.EventTag;
import sk.liptovzije.core.service.tag.ITagService;
import sk.liptovzije.utils.exception.InvalidRequestException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/eventtag")
public class EventTagApi {
    private ITagService tagService;

    @Autowired
    public EventTagApi(ITagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity createTag(@Valid @RequestBody EventTagParam tagParam, BindingResult bindingResult) {
        checkInput(tagParam, bindingResult);
        EventTag tag = tagParam.toDo();
        EventTagParam response = tagService.save(tag).map(EventTagParam::new).orElseThrow(InternalError::new);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity listTags() {
        List<EventTag> userList = tagService.getAll();
        return ResponseEntity.ok(this.tagResponse(userList));
    }

    private void checkInput(@Valid @RequestBody EventTagParam tagParam, BindingResult bindingResult) {
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

    private Map<String, List> tagResponse(List<EventTag> tags) {
        List<EventTagParam> params = tags.stream()
                .map(EventTagParam::new)
                .collect(Collectors.toList());

        return new HashMap<String, List>() {{
            put("tags", params);
        }};
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class EventTagParam {
    private Long id;
    @NotBlank(message = "can't be empty")
    private String label;

    EventTagParam(EventTag domainObject) {
        this.id = domainObject.getId();
        this.label = domainObject.getLabel();
    }

    EventTag toDo() {
        EventTag tag = new EventTag();
        tag.setId(this.id);
        tag.setLabel(this.label);
        return tag;
    }
}