package sk.liptovzije.core.service.authorization;

import org.springframework.stereotype.Service;
import sk.liptovzije.application.event.Event;
import sk.liptovzije.application.place.Place;
import sk.liptovzije.application.user.Roles;
import sk.liptovzije.application.user.User;

@Service
public class AuthorizationService implements IAuthorizationService {
    @Override
    public boolean canApproveEvent(User user) {
        return user.getRole().equals(Roles.ROLE_ADMIN) ||
               user.getRole().equals(Roles.ROLE_TRUSTED_USER);
    }

    @Override
    public boolean canModifyEvent(User user, Event event) {
        return user.getRole().equals(Roles.ROLE_ADMIN) || event.getOwnerId().equals(user.getId());
    }

    @Override
    public boolean canModifyPlace(User user, Place place) {
        return user.getRole().equals(Roles.ROLE_ADMIN);
    }
}
