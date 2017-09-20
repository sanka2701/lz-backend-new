package sk.liptovzije.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.stereotype.Component;
import sk.liptovzije.model.DO.UserDO;
import sk.liptovzije.model.Response;
import sk.liptovzije.service.IJwtService;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Component
public class JwtServiceImpl implements IJwtService{
    private static final String ISSUER = "http://liptovzije.sk";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final String SECRET = "H7PP2i+cxNQrDQOxq7KhhKuQX9HkvbsVpzrwMzyLRAQ=";
    private static final String SIGN = "Bearer";
    private static final Long TTL = 600000L;

    public Response<UserDO> sign(UserDO user){
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + TTL;    // expiration set for 10 minutes

        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        byte[] key = TextCodec.BASE64.decode(SECRET);

        JwtBuilder builder = Jwts.builder()
                .setId(Long.toString(user.getId()))
                .setIssuedAt(now)
                .setExpiration(exp)
                .setIssuer(ISSUER)
                .setSubject(user.getCredentials().getUsername())
                .signWith(SIGNATURE_ALGORITHM, key)
                .claim("role", user.getRole());

        String jwt = builder.compact();
        System.out.println("JWT: " + jwt);

        return new Response<>(user, jwt);
    }

    public boolean verify(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(jwt).getBody();
            System.out.println("ID: " + claims.getId());
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Issuer: " + claims.getIssuer());
            System.out.println("Expiration: " + claims.getExpiration());
            System.out.println("Role: " + claims.get("role"));
        } catch (SignatureException e) {
            System.out.println("SECURITY v <>");
            return false;
        }

        return true;
    }
}
