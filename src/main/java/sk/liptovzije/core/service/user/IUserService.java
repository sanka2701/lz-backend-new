package sk.liptovzije.core.service.user;

import sk.liptovzije.application.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> save(User user);

    Optional<User> update(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
