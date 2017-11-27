package sk.liptovzije.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sk.liptovzije.model.DTO.UserDTO;
import sk.liptovzije.service.IJwtService;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

import static sk.liptovzije.security.SecurityConstants.ISSUER;
import static sk.liptovzije.security.SecurityConstants.SECRET;
import static sk.liptovzije.security.SecurityConstants.TTL;

@Service
public class JwtServiceImpl implements IJwtService{

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    public String sign(UserDTO user){
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + TTL;    // expiration set for 10 minutes

        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        byte[] key = TextCodec.BASE64.decode(SECRET);

        JwtBuilder builder = Jwts.builder()
                .setId(Long.toString(user.getId()))
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .setIssuer(ISSUER)
                .signWith(SIGNATURE_ALGORITHM, key)
                .claim("role", user.getRole());

        String jwt = builder.compact();
        System.out.println("JWT: " + jwt);

        return jwt;
    }

    public Claims verify(String jwt) {
        Claims claims = null;

        try {
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(jwt).getBody();

            log.debug("ID: " + claims.getId());
            log.debug("Subject: " + claims.getSubject());
            log.debug("Issuer: " + claims.getIssuer());
            log.debug("Expiration: " + claims.getExpiration());
            log.debug("Role: " + claims.get("role"));

            return claims;
        } catch (SignatureException e) {
            System.out.println("SECURITY v <>");
            e.printStackTrace();
            return null;
        }
    }
}
