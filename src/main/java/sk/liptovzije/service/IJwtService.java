package sk.liptovzije.service;

import io.jsonwebtoken.Claims;
import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.DTO.UserDTO;

public interface IJwtService {
    String sign(UserDTO user);
    Claims verify(String token);
}
