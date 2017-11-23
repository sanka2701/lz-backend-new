package sk.liptovzije.service;

import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DTO.UserDTO;

public interface IJwtService {
    String sign(UserDTO user);
    String sign(UserDO user);
    boolean verify(String token);
}
