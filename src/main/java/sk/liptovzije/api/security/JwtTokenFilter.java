package sk.liptovzije.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.liptovzije.core.service.jwt.IJwtService;
import sk.liptovzije.core.service.user.IUserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private IUserService userService;

    @Autowired
    private IJwtService jwtService;

    private String header = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getTokenString(request.getHeader(header)).ifPresent(token -> {
            jwtService.getSubFromToken(token).ifPresent(id -> {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    userService.findById(Long.parseLong(id)).ifPresent(user -> {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Stream.of(new SimpleGrantedAuthority(user.getRole())).collect(Collectors.toSet())
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    });
                }
            });
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenString(String header) {
        if (header == null || header.split("").length < 2) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(header.split(" ")[1]);
        }
    }
}

