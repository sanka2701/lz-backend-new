package sk.liptovzije.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sk.liptovzije.model.DO.UserCredentialsDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.model.DTO.UserDTO;
import sk.liptovzije.service.IAuthenticationService;
import sk.liptovzije.service.ICredentialService;
import sk.liptovzije.service.IUserService;

import java.util.ArrayList;
import java.util.List;

public class LZAuthenticationManager implements AuthenticationManager {
    @Autowired
    ICredentialService credentialService;

    @Autowired
    IUserService userService;

    @Autowired
    IAuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken auth;
        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        UserCredentialsDTO receivedCredentials = new UserCredentialsDTO(username, password);
        UserCredentialsDO  loadedCredentials = credentialService.getByUsername(username);

        if(loadedCredentials == null || !authenticationService.validateCredentials(loadedCredentials, receivedCredentials)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDTO user = new UserDTO(userService.getById(loadedCredentials.getUserId()));
        user.setUsername(loadedCredentials.getUsername());

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(user.getRole().toUpperCase()));

        auth = new UsernamePasswordAuthenticationToken(username, null, grantedAuths);
        auth.setDetails(user);
        return auth;
    }
}
