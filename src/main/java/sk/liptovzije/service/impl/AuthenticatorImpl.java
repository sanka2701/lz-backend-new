package sk.liptovzije.service.impl;

import org.springframework.stereotype.Component;
import sk.liptovzije.model.DO.UserCredentials;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.service.IAuthenticationService;
/**
 * Created by jan.husenica on 8/29/2016.
 */
@Component
public class AuthenticatorImpl implements IAuthenticationService {

    @Override
    public boolean validCredentials(UserCredentials original, UserCredentialsDTO checkedDTO) {
        UserCredentials checked = new UserCredentials(checkedDTO, original.getSalt());

        return original.equals(checked);
    }
}
