package sk.liptovzije.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sk.liptovzije.model.DTO.UserCredentialsDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static sk.liptovzije.security.SecurityConstants.HEADER_STRING;
import static sk.liptovzije.security.SecurityConstants.ORIGIN_HEADER;
import static sk.liptovzije.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authManager) {
        this.setFilterProcessesUrl("/user/authenticate");
        this.authenticationManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        try {
            // todo: because of CORS
            if (httpServletRequest.getHeader(ORIGIN_HEADER) != null) {
                String origin = httpServletRequest.getHeader(ORIGIN_HEADER);
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
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        //todo: probably unnecessary - remove
        int a =5 ;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        //todo create token
        String token = "JWT token";
        User tmp = (User)auth.getPrincipal();
//        String token = Jwts.builder()
//                .setSubject(((User) auth.getPrincipal()).getUsername())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
//                .compact();

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
