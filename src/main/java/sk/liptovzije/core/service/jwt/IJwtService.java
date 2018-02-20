package sk.liptovzije.core.service.jwt;

import org.springframework.stereotype.Service;
import sk.liptovzije.application.user.User;

import java.util.Optional;

@Service
public interface IJwtService {
    String toToken(User user);

    Optional<String> getSubFromToken(String token);
}
