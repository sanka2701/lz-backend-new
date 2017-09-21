package sk.liptovzije.service;

import sk.liptovzije.model.DO.UserCredentialsDO;

public interface ICredentialService {
    Long save(UserCredentialsDO credentials);
    void update(UserCredentialsDO credentials);
    UserCredentialsDO getByUserId (Long userId);
    UserCredentialsDO getByUsername (String username);
    void delete (Long userId);
}
