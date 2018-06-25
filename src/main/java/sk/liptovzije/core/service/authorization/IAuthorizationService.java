package sk.liptovzije.core.service.authorization;

import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.place.Place;
import sk.liptovzije.application.user.User;

public interface IAuthorizationService {
    boolean canApproveEvent(User user);

    boolean canModifyEvent(User user, Event event);

    boolean canModifyPlace(User user, Place place);
}
