package com.manastudio.Features.Users.Services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.manastudio.Abstractions.Result;
import com.manastudio.Abstractions.VoidResult;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;
@Service
public class UserManipulatingService {

    @Autowired
    private UserFetchingService userFetchingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Change Email
    public Result<VoidResult> changeUserEmailById(Long userId, String newEmail) {
        Result<User> fetchedUserResults = userFetchingService.fetchUserById(userId);
        if (fetchedUserResults.isFailure()) {
            return Result.failure(fetchedUserResults.getException());
        }

        User user = fetchedUserResults.getValue();

        if (user.getEmail().equalsIgnoreCase(newEmail)) {
            return Result.failure(new IllegalArgumentException("The new email is the same as the current email."));
        }

        if (userRepository.existsByEmail(newEmail)) {
            return Result.failure(new IllegalArgumentException("The email address is already in use."));
        }

        user.setEmail(newEmail);
        userRepository.save(user);

        return Result.success(VoidResult.create());
    }

    // Change Username
    public Result<VoidResult> changeUserNameById(Long userId, String newUsername) {
        Result<User> fetchedUserResults = userFetchingService.fetchUserById(userId);
        if (fetchedUserResults.isFailure()) {
            return Result.failure(fetchedUserResults.getException());
        }

        User user = fetchedUserResults.getValue();

        if (user.getUsername().equalsIgnoreCase(newUsername)) {
            return Result.failure(new IllegalArgumentException("The new username is the same as the current username."));
        }

        if (userRepository.existsByUsername(newUsername)) {
            return Result.failure(new IllegalArgumentException("The username is already in use."));
        }

        user.setUsername(newUsername);
        userRepository.save(user);

        return Result.success(VoidResult.create());
    }

    // Change Birthdate
    public Result<VoidResult> changeUserBirthdateById(Long userId, LocalDate newBirthdate) {
        Result<User> fetchedUserResults = userFetchingService.fetchUserById(userId);
        if (fetchedUserResults.isFailure()) {
            return Result.failure(fetchedUserResults.getException());
        }

        User user = fetchedUserResults.getValue();

        if (user.getDateOfBirth().equals(newBirthdate)) {
            return Result.failure(new IllegalArgumentException("The new birthdate is the same as the current birthdate."));
        }

        user.setDateOfBirth(newBirthdate);
        userRepository.save(user);

        return Result.success(VoidResult.create());
    }

    // Change Password
    public Result<VoidResult> changeUserPasswordById(Long userId, String newPassword) {
        Result<User> fetchedUserResults = userFetchingService.fetchUserById(userId);
        if (fetchedUserResults.isFailure()) {
            return Result.failure(fetchedUserResults.getException());
        }

        User user = fetchedUserResults.getValue();

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return Result.failure(new IllegalArgumentException("The new password is the same as the current password."));
        }

        user.setPassword(encodedNewPassword);
        userRepository.save(user);

        return Result.success(VoidResult.create());
    }
}