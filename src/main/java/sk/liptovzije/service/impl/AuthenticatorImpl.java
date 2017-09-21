package sk.liptovzije.service.impl;

import org.springframework.stereotype.Component;
import sk.liptovzije.model.DO.UserCredentialsDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.service.IAuthenticationService;
/**
 * Created by jan.husenica on 8/29/2016.
 */
@Component
public class AuthenticatorImpl implements IAuthenticationService {

    @Override
    public boolean validateCredentials(UserCredentialsDO original, UserCredentialsDTO checkedDTO) {
        UserCredentialsDO received = new UserCredentialsDO(checkedDTO, original.getSalt());

        return original.equals(received);
    }
}
