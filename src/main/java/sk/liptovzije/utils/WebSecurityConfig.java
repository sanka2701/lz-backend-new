package sk.liptovzije.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/user/authenticate").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new LoginFilter("/user/authenticate", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class);
                // And filter other requests to check the presence of JWT in header
//                .addFilterBefore(new JWTAuthenticationFilter(),
//                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Create a default account
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("pass")
                .roles("ADMIN");
    }
}
