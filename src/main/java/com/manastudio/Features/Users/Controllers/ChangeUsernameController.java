package com.manastudio.Features.Users.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.manastudio.Abstractions.Result;
import com.manastudio.Abstractions.VoidResult;
import com.manastudio.Features.Users.Dtos.Edit.ChangeUsernameRequest;
import com.manastudio.Features.Users.Services.CustomUserDetails;
import com.manastudio.Features.Users.Services.UserManipulatingService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/edit/username")
public class ChangeUsernameController {

    @Autowired
    private UserManipulatingService userManipulatingService;

    @GetMapping
    public String showChangeUsernameForm(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Model model) {
        // Add an empty form object to the model
        if (!model.containsAttribute("usernameForm")) {
            ChangeUsernameRequest usernameForm = new ChangeUsernameRequest();
            usernameForm.setNewUsername(currentUser.getUsername()); // Prepopulate with current username
            model.addAttribute("usernameForm", usernameForm);
        }
        return "User/editUsername";
    }


    @PostMapping
    public String changeUsername(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @ModelAttribute @Valid ChangeUsernameRequest usernameForm,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "User/editUsername"; // Return to the form if there are validation errors
        }

        Long userId = currentUser.getId();
        Result<VoidResult> changeUsernameResult = userManipulatingService.changeUserNameById(userId, usernameForm.getNewUsername());

        if (changeUsernameResult.isFailure()) {
            model.addAttribute("error", changeUsernameResult.getException().getMessage());
            model.addAttribute("usernameForm", usernameForm); // Ensure the form is added back to the model
            return "User/editUsername";
        }

        model.addAttribute("success", "Username updated successfully!");
        return "redirect:/user/profile?userId=" + userId;
    }

}
