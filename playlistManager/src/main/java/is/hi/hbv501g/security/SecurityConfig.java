package is.hi.hbv501g.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // For this project we keep it simple: no CSRF tokens for API calls
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // allow static files and index
                        .requestMatchers("/", "/index.html", "/static/**").permitAll()
                        // allow auth endpoints without being logged in
                        .requestMatchers("/api/auth/**").permitAll()
                        // everything else is allowed – you use your own checks in controllers
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
