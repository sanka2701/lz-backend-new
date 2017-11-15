package sk.liptovzije.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import sk.liptovzije.service.ICredentialService;

@Configuration
@EnableWebSecurity
@ComponentScan("sk.liptovzije")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ICredentialService credentialService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http./*cors().disable().*/csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/create").permitAll()
                .antMatchers(HttpMethod.POST, "/user/authenticate").permitAll()
                .anyRequest().authenticated()
                .and()
            .httpBasic()
                .and()
            // authentication (login)
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            // authorization (has right)
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Create a default account
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password("pass")
//                .roles("ADMIN");
        auth.userDetailsService(credentialService);
    }
}
