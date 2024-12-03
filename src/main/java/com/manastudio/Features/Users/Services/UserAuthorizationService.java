package com.manastudio.Features.Users.Services;




import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Users.Dtos.Auth.RegisterRequestDto;
import com.manastudio.Features.Users.Exceptions.UserAlreadyExistsException;
import com.manastudio.Features.Users.Exceptions.UserNotFoundException;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthorizationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Load User by Username for Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + username);
        }

        // Use the CustomUserDetails class to include firstName, lastName, email, and other fields
        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(), // Pass the dateOfBirth
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    /**
     * Register a new user
     */
    public Result<Long> registerUser(RegisterRequestDto registerRequestDto) {
        String username = registerRequestDto.getUsername();
        String email = registerRequestDto.getEmail();

        // Check if username or email already exists
        if (userRepository.findByUsername(username) != null) {
            return Result.from(new UserAlreadyExistsException("Username already exists."));
        }
        if (userRepository.findByEmail(email) != null) {
            return Result.from(new UserAlreadyExistsException("Email already exists."));
        }

        try {
            User newUser = new User();
            newUser.setFirstName(registerRequestDto.getFirstName());
            newUser.setLastName(registerRequestDto.getLastName());
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            newUser.setDateOfBirth(registerRequestDto.getDateOfBirth());
            newUser.setRole("USER");

            User savedUser = userRepository.save(newUser);
            return Result.from(savedUser.getId());
        } catch (Exception e) {
            return Result.from(e);
        }
    }
}