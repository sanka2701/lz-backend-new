package sk.liptovzije.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sk.liptovzije.service.ICredentialService;
import sk.liptovzije.service.IJwtService;

@Configuration
@EnableWebSecurity
@ComponentScan("sk.liptovzije")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ICredentialService credentialService;

    @Autowired
    private IJwtService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http/*.cors().and()*/.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/create").permitAll()
                .anyRequest().authenticated()
                .and()
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
//                .addFilter(new JWTAuthorizationFilter(authenticationManager()))

                .addFilter(new JWTAuthenticationFilter(authManager(), jwtService))
                .addFilter(new JWTAuthorizationFilter(authManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .and()
//            .formLogin()
//                .loginProcessingUrl("/user/authenticate")
//                .permitAll();
//                .usernameParameter("username") // default is username
//                .passwordParameter("password") // default is password
//                .loginPage("/authentication/login") // default is /login with an HTTP get
//                .failureUrl("/authentication/login?failed") // default is /login?error
//                .loginProcessingUrl("/authentication/login/process"); // default is /login
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

    @Bean
    AuthenticationManager authManager(){
        return new LZAuthenticationManager();
    }

    //todo reevalute with usage of cors filter usage
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
