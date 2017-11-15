package sk.liptovzije.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import sk.liptovzije.model.DO.UserCredentialsDO;

public interface ICredentialService extends UserDetailsService{
    Long save(UserCredentialsDO credentials);
    void update(UserCredentialsDO credentials);
    UserCredentialsDO getByUserId (Long userId);
    UserCredentialsDO getByUsername (String username);
    void delete (Long userId);
}
