package com.amatista.tienda.config;

import com.amatista.tienda.model.AppUser;
import com.amatista.tienda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner seedAdmin(UserRepository repo,
                                PasswordEncoder encoder,
                                @Value("${app.admin.username}") String username,
                                @Value("${app.admin.password}") String password) {
        return args -> {
            if (repo.existsByUsername(username)) return;

            repo.save(AppUser.builder()
                    .username(username)
                    .passwordHash(encoder.encode(password))
                    .role("ADMIN")
                    .build());
        };
    }
}