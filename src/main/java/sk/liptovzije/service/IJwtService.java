package sk.liptovzije.service;

import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.Response;

public interface IJwtService {
    Response sign(UserDO user);
    boolean verify(String token);
}