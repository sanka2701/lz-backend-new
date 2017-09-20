package sk.liptovzije.service;

import sk.liptovzije.model.DO.UserCredentials;
import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.model.Response;

/**
 * Created by jan.husenica on 8/29/2016.
 */
public interface IAuthenticationService {
    boolean validCredentials(UserCredentials original, UserCredentialsDTO checked);
}
