package sk.liptovzije.security;

import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityConstants {
    public static final String TOKEN_PREFIX  = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ORIGIN_HEADER = "Origin";
    public static final String ISSUER        = "http://liptovzije.sk";
    public static final Long   TTL           = 864000000L; // 10 days
    public static final String SECRET = "H7PP2i+cxNQrDQOxq7KhhKuQX9HkvbsVpzrwMzyLRAQ=";
}
