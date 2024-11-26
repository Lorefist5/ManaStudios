package com.manastudio.Features.Common;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("title", "Welcome to Manastudio");
        model.addAttribute("message", "This is the home page of your application.");
        return "Common/home"; // This will map to the home.html template
    }
}