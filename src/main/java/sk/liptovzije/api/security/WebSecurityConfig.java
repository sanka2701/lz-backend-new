package sk.liptovzije.api.security;

import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static java.util.Arrays.asList;
import static sk.liptovzije.application.user.Roles.ADMIN;
import static sk.liptovzije.application.user.Roles.TRUSTED_USER;
import static sk.liptovzije.application.user.Roles.USER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors()
            .and()
            .exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint("Unauthenticated"))
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()

            .antMatchers(HttpMethod.POST, "/potw").hasRole(ADMIN)
            .antMatchers(HttpMethod.DELETE, "/potw").hasRole(ADMIN)
            .antMatchers(HttpMethod.POST, "/potw/update").hasRole(ADMIN)
            .antMatchers(HttpMethod.GET, "/potw/list").permitAll()
            .antMatchers(HttpMethod.GET, "/potw").permitAll()

            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers(HttpMethod.POST, "/articles").hasRole(ADMIN)
            .antMatchers(HttpMethod.DELETE, "/articles").hasRole(ADMIN)
            .antMatchers(HttpMethod.POST, "/articles/update").hasRole(ADMIN)
            .antMatchers(HttpMethod.POST, "/articles/filter").permitAll()
            .antMatchers(HttpMethod.GET, "/articles").permitAll()

            .antMatchers(HttpMethod.DELETE, "/events").hasRole(ADMIN)
            .antMatchers(HttpMethod.POST, "/events/filter").permitAll()
            .antMatchers(HttpMethod.GET, "/events").permitAll()

            .antMatchers(HttpMethod.POST, "/eventtag").hasAnyRole(ADMIN, TRUSTED_USER)
            .antMatchers(HttpMethod.POST, "/eventtag/update").hasAnyRole(ADMIN, TRUSTED_USER)
            .antMatchers(HttpMethod.DELETE, "/eventtag").hasAnyRole(ADMIN, TRUSTED_USER)
            .antMatchers(HttpMethod.GET, "/eventtag/id").permitAll()
            .antMatchers(HttpMethod.GET, "/eventtag").permitAll()

            .antMatchers(HttpMethod.POST,"/users", "/users/login").permitAll()
            .antMatchers(HttpMethod.POST, "/users/filter").permitAll()
//            .antMatchers(HttpMethod.POST, "/users/filter").hasRole(ADMIN)

            .antMatchers(HttpMethod.GET, "/img/**").permitAll()

            .antMatchers(HttpMethod.GET, "/places").permitAll()
            .antMatchers(HttpMethod.GET, "/places/id").permitAll()
            .antMatchers(HttpMethod.GET, "/places/list").permitAll()

            //todo: This is just for inmemory H2 DB -> remove in production
            .antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated();

        // todo: Frame options are needed to prevent a browser to load HTML page in an <iframe> or a <frame> tag and for H2 Console page to load it needs to be disabled
        http.headers().frameOptions().disable();
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(asList("*"));
        configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
