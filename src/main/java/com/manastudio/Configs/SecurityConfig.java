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
                .requestMatchers("/", "/movies","/movies/{movieId}", "/register", "/css/**", "/js/**").permitAll()
                .requestMatchers("/").permitAll()// Allow public access to home, register, and static resources
                .anyRequest().authenticated() // Require authentication for all other endpoints
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/",true)// Use custom login page // Redirect authenticated users to the home page
                    .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Specify logout URL
                .logoutSuccessUrl("/login?logout=true") // Redirect to login page after logout
                .permitAll())
            .csrf(csrf -> csrf.disable()); // Optional: Disable CSRF if needed for debugging or specific use cases

        return http.build();
    }
}