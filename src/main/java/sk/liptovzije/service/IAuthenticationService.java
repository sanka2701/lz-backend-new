package sk.liptovzije.service;

import sk.liptovzije.model.DO.UserCredentialsDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;

/**
 * Created by jan.husenica on 8/29/2016.
 */
public interface IAuthenticationService {
    boolean validateCredentials(UserCredentialsDO stored, UserCredentialsDTO received);
}
