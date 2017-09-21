package sk.liptovzije.service.impl;

import org.springframework.stereotype.Service;
import sk.liptovzije.model.DO.UserCredentialsDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.service.IAuthenticationService;
/**
 * Created by jan.husenica on 8/29/2016.
 */
@Service
public class AuthenticatorImpl implements IAuthenticationService {

    @Override
    public boolean validateCredentials(UserCredentialsDO originalPassword, UserCredentialsDTO checkedPasswordDTO) {
        UserCredentialsDO received = checkedPasswordDTO.toDo(originalPassword.getUserId(), originalPassword.getSalt());
        return originalPassword.equals(received);
    }
}