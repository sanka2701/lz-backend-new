package sk.liptovzije.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.model.DTO.UserDTO;
import sk.liptovzije.service.IJwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import static sk.liptovzije.security.SecurityConstants.HEADER_STRING;
import static sk.liptovzije.security.SecurityConstants.ORIGIN_HEADER;
import static sk.liptovzije.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private IJwtService jwtService;
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authManager, IJwtService jwtService) {
        this.setFilterProcessesUrl("/user/authenticate");
        this.authenticationManager = authManager;
        this.jwtService            = jwtService;
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
                httpServletResponse.addHeader("Access-Control-Expose-Headers", "Authorization");
                httpServletResponse.addHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
            }
            if (httpServletRequest.getMethod().equals("OPTIONS")) {
                httpServletResponse.getWriter().print("OK");
                httpServletResponse.getWriter().flush();
                return null;
            }
            //todo: test purrposes only -> may even remove apache maven repository
            //String marshalledXml = org.apache.commons.io.IOUtils.toString(httpServletRequest.getInputStream());

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

        UserDTO user = (UserDTO) auth.getDetails();
        ObjectMapper mapper = new ObjectMapper();
        String userJsonString = mapper.writeValueAsString(user);
        String token = jwtService.sign(user);

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.getWriter().write(userJsonString);
        res.getWriter().flush();
        res.getWriter().close();
    }
}
