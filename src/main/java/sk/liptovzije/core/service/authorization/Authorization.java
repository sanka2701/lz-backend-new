package sk.liptovzije.core.service.authorization;

import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.place.Place;
import sk.liptovzije.application.user.Roles;
import sk.liptovzije.application.user.User;

@Service
public class Authorization {

    public static boolean canApproveEvent(User user) {
        return user.getRole().equals(Roles.ROLE_ADMIN);
    }

    public static boolean canModifyEvent(User user, Event event) {
        return user.getRole().equals(Roles.ROLE_ADMIN) || event.getOwnerId().equals(user.getId());
    }

    public static boolean canModifyPlace(User user, Place place) {
        return user.getRole().equals(Roles.ROLE_ADMIN);
    }
}
