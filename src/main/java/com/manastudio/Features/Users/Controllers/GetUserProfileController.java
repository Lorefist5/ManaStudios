package com.manastudio.Features.Users.Controllers;

import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Services.UserFetchingService;
import com.manastudio.Abstractions.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GetUserProfileController {

    @Autowired
    private UserFetchingService userFetchingService;

    @GetMapping("/user/profile")
    public String getUserProfile(@RequestParam("userId") Long userId, Model model) {
        Result<User> userResult = userFetchingService.fetchUserById(userId);

        if (userResult.isFailure()) {
            model.addAttribute("error", "User not found.");
            return "Common/error";
        }

        model.addAttribute("user", userResult.getValue());
        return "User/profile";
    }
}