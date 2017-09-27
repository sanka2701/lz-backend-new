package sk.liptovzije.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import sk.liptovzije.model.DTO.UserCredentialsDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    static final String ORIGIN = "Origin";
    
    public LoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
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

        UserCredentialsDTO creds = new ObjectMapper()
                .readValue(httpServletRequest.getInputStream(), UserCredentialsDTO.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getUsername(),
                        creds.getPassword(),
                        Collections.emptyList()
                )
        );
    }
}
