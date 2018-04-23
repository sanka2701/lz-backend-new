package sk.liptovzije.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.liptovzije.core.service.event.IEventService;

@RestController
@RequestMapping(path = "/events")
public class EventsApi {

    private IEventService eventService;

    @Autowired
    public EventsApi(IEventService eventService) {
        this.eventService = eventService;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class EventParam {
        @NotBlank(message = "can't be empty")
        private String username;
        @NotBlank(message = "can't be empty")
        private String password;
    }
}
