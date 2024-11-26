package com.manastudio.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/register", "/css/**", "/js/**").permitAll() // Allow public access to register and static resources
                .anyRequest().authenticated() // Require authentication for other endpoints
            )
            .formLogin(form -> form
                    .loginPage("/login") // Use custom login page
                    .defaultSuccessUrl("/", true) // Redirect to home page after successful login
                    .permitAll()
                )
            .logout(logout -> logout
                .logoutUrl("/logout") // Specify logout URL
                .logoutSuccessUrl("/login?logout=true") // Redirect to login page with logout parameter
                .permitAll());

        return http.build();
    }
}