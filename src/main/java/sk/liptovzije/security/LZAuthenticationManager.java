package sk.liptovzije.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import sk.liptovzije.model.DO.UserCredentialsDO;
import sk.liptovzije.model.DTO.UserCredentialsDTO;
import sk.liptovzije.service.IAuthenticationService;
import sk.liptovzije.service.ICredentialService;

import java.util.Collections;

public class LZAuthenticationManager implements AuthenticationManager {
    @Autowired
    ICredentialService credentialService;

    @Autowired
    IAuthenticationService authenticationService;
//    @Autowired
//    LZAuthenticationManager(ICredentialService credentialService) {
//        this.credentialService = credentialService;
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        UserCredentialsDTO receivedCredentials = new UserCredentialsDTO(username, password);
        UserCredentialsDO  loadedCredentials = credentialService.getByUsername(username);

        Authentication response = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());

        if(loadedCredentials == null || !authenticationService.validateCredentials(loadedCredentials, receivedCredentials)) {
            response.setAuthenticated(Boolean.FALSE);
        } else {
            response.setAuthenticated(Boolean.TRUE);
        }

        return response;
    }
}
