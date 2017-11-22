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
//todo: spring security took care of this - reevaluate usage
    @Override
    public boolean validateCredentials(UserCredentialsDO stored, UserCredentialsDTO receivedDTO) {
        UserCredentialsDO received = receivedDTO.toDo(stored.getUserId(), stored.getSalt());
        return stored.equals(received);
    }
}