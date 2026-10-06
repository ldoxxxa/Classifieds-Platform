package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

@Service
public class BenutzerUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BenutzerService benutzerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if ("admin".equals(username)) {
            return User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMINISTRATOR")
                .build();
        }

        Benutzer benutzer = benutzerService.findBenutzerById(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.withUsername(benutzer.getLoginName())
            .password(benutzer.getPasswort())     // bereits encoded in der DB
            .roles(benutzer.getRolle())
            .build();
    }
}