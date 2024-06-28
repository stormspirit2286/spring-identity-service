package com.duynguyen.identityservice.configuration;

import com.duynguyen.identityservice.entity.User;
import com.duynguyen.identityservice.enums.Role;
import com.duynguyen.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            var user = userRepository.findByUsername("admin");
            if (user.isEmpty()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                var password = passwordEncoder.encode("123456");
                User userCreated = User.builder()
                        .username("admin")
                        .password(password)
                        .roles(roles)
                        .build();
                userRepository.save(userCreated);
                log.info("Created admin user");
            } else {
                log.info("User already exists: {}", user.get().getRoles());
            }
        };
    }
}
