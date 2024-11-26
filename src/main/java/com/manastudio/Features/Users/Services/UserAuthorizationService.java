package com.manastudio.Features.Users.Services;




import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Users.Dtos.Auth.RegisterRequestDto;
import com.manastudio.Features.Users.Exceptions.UserAlreadyExistsException;
import com.manastudio.Features.Users.Exceptions.UserNotFoundException;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
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

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // The password is assumed to be already encoded
                .roles(user.getRole()) // Assign the user's role
                .build();
    }

    /**
     * Register a new user
     * Returns a `Result<Long>`: User ID if successful, or an exception wrapped in the `Result`.
     */
    public Result<Long> registerUser(RegisterRequestDto registerRequestDto) {
        // Extract fields from the DTO
        String username = registerRequestDto.getUsername();
        String email = registerRequestDto.getEmail();

        // Check if username already exists
        if (userRepository.findByUsername(username) != null) {
            return Result.from(new UserAlreadyExistsException("Username already exists."));
        }

        // Check if email already exists
        if (userRepository.findByEmail(email) != null) {
            return Result.from(new UserAlreadyExistsException("Email already exists."));
        }

        try {
            // Create a new User object
            User newUser = new User();
            newUser.setFirstName(registerRequestDto.getFirstName());
            newUser.setLastName(registerRequestDto.getLastName());
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(registerRequestDto.getPassword())); // Encode the password
            newUser.setDateOfBirth(registerRequestDto.getDateOfBirth()); // Set date of birth
            newUser.setRole("USER"); // Default role

            // Save the user and return their ID
            User savedUser = userRepository.save(newUser);
            return Result.from(savedUser.getId()); // Success case
        } catch (Exception e) {
            // Return any unexpected exceptions as a failure result
            return Result.from(e);
        }
    }
}