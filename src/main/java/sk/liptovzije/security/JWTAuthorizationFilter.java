package sk.liptovzije.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sk.liptovzije.service.IJwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sk.liptovzije.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private IJwtService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, IJwtService jwtService) {
        super(authManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        // todo: because of CORS
        if (req.getHeader(ORIGIN_HEADER) != null) {
            String origin = req.getHeader(ORIGIN_HEADER);
            res.addHeader("Access-Control-Allow-Origin", origin);
            res.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            res.addHeader("Access-Control-Allow-Credentials", "true");
            res.addHeader("Access-Control-Expose-Headers", "Authorization");
            res.addHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        }
        if (req.getMethod().equals("OPTIONS")) {
            res.getWriter().print("OK");
            res.getWriter().flush();
            return;
        }

        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
        if (token != null) {
            Claims claims = jwtService.verify(token);
            if (claims != null) {
                String username = claims.getSubject();
                String role = (String)claims.get("role");

                List<GrantedAuthority> grantedAuths = new ArrayList<>();
                grantedAuths.add(new SimpleGrantedAuthority(role.toUpperCase()));

                return new UsernamePasswordAuthenticationToken(username, null, grantedAuths);
            }
            return null;
        }
        return null;
    }

}
