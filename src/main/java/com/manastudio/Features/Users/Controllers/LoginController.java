package com.manastudio.Features.Users.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Show the login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "Auth/login"; // Render the Auth/login.html template
    }

    // Optional: Handle logout redirection
    @GetMapping("/logout-success")
    public String logoutPage() {
        return "Auth/logout-success"; // Render the Auth/logout-success.html template (optional)
    }
}