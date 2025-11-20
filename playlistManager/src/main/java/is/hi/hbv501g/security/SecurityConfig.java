package is.hi.hbv501g.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // For this project we keep it simple: no CSRF tokens for API calls
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // allow static files and index.html for everyone
                        .requestMatchers("/", "/index.html", "/static/**").permitAll()

                        // allow auth endpoints without being logged in
                        .requestMatchers("/api/auth/**").permitAll()

                        // allow anonymous users to *view/stream* tracks
                        .requestMatchers(HttpMethod.GET, "/api/tracks/**").permitAll()

                        // require login to *create/modify* tracks
                        .requestMatchers(HttpMethod.POST, "/api/tracks/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,  "/api/tracks/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/tracks/**").authenticated()

                        // require login for ANY playlist-related operation
                        // (create playlist, list "mine", add/remove tracks, markers, delete, copy, etc.)
                        .requestMatchers("/api/playlists/**").authenticated()

                        // everything else (if added later and not matched above) is allowed
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
