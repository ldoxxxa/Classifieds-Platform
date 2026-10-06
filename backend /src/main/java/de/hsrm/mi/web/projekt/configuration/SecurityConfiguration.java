package de.hsrm.mi.web.projekt.configuration;

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(toH2Console()).permitAll()
                .requestMatchers("/api/**", "/stompbroker/**").permitAll()
                .requestMatchers("/admin/benutzer/**").hasRole("ADMINISTRATOR")
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(folo -> folo.defaultSuccessUrl("/admin/anzeige", true))
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(toH2Console())
                .ignoringRequestMatchers("/admin/benutzer/*/hx/feld/*")
                .ignoringRequestMatchers("/api/**")
            )
            .headers(hdrs -> hdrs.frameOptions(fo -> fo.sameOrigin()))
            .build();
    }
}