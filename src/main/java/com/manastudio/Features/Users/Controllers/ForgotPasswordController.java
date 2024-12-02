package com.manastudio.Features.Users.Controllers;

import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "Auth/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model
    ) {
        // Convert string birthdate to LocalDate

    	LocalDate dob = LocalDate.parse(birthdate);
        // Fetch the user from the database
        User user = userRepository.findByUsername(username);

        // Validate user details
        if (user == null || !user.getEmail().equals(email) || !user.getDateOfBirth().equals(dob)) {
            model.addAttribute("error", "User details do not match our records.");
            return "Auth/forgotPassword";
        }

        // Check if the passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "Auth/forgotPassword";
        }

        // Encode and update the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("success", "Your password has been successfully reset. You can now log in with your new password.");
        return "Auth/Login";
    }
}