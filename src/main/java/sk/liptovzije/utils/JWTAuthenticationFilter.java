package sk.liptovzije.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import sk.liptovzije.model.DTO.UserCredentialsDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String ORIGIN = "Origin";
    //todo: remove and move to token creation
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        try {
            // todo: because of CORS
            if (httpServletRequest.getHeader(ORIGIN) != null) {
                String origin = httpServletRequest.getHeader(ORIGIN);
                httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
                httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
                httpServletResponse.addHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
            }
            if (httpServletRequest.getMethod().equals("OPTIONS")) {
                httpServletResponse.getWriter().print("OK");
                httpServletResponse.getWriter().flush();
                return null;
            }

            UserCredentialsDTO creds = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserCredentialsDTO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        //todo create token
        String token = "JWT token";
//        String token = Jwts.builder()
//                .setSubject(((User) auth.getPrincipal()).getUsername())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
//                .compact();

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
