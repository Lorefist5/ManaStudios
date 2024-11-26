package com.manastudio.Features.Users.Controllers;


import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Users.Dtos.Auth.RegisterRequestDto;

import com.manastudio.Features.Users.Services.UserAuthorizationService;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    private final UserAuthorizationService userAuthorizationService;

    public RegistrationController(UserAuthorizationService userAuthorizationService) {
        this.userAuthorizationService = userAuthorizationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequestDto", new RegisterRequestDto());
        return "Auth/register"; // Return the registration form
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("registerRequestDto") @Validated RegisterRequestDto registerRequestDto,
            BindingResult bindingResult, // BindingResult for validation errors
            Model model) {

        // Check if the form has validation errors
        if (bindingResult.hasErrors()) {
            return "Auth/register"; // Redirect back to the registration form with errors
        }

        // Validate password confirmation
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "Auth/register";
        }

        // Call the service to register the user
        Result<Long> result = userAuthorizationService.registerUser(registerRequestDto);

        if (result.isFailure()) {
            model.addAttribute("error", result.getException().getMessage());
            return "Auth/register";
        }

        model.addAttribute("success", "User registered successfully!");
        return "Auth/login"; // Redirect to login page after success
    }
}