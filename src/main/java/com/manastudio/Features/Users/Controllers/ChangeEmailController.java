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
import com.manastudio.Features.Users.Dtos.Edit.ChangeEmailRequest;
import com.manastudio.Features.Users.Services.CustomUserDetails;
import com.manastudio.Features.Users.Services.UserManipulatingService;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/user/edit/email")
public class ChangeEmailController {

    @Autowired
    private UserManipulatingService userManipulatingService;

    @GetMapping
    public String showChangeEmailForm(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        if (!model.containsAttribute("emailForm")) {
            ChangeEmailRequest emailForm = new ChangeEmailRequest();
            emailForm.setNewEmail(currentUser.getEmail()); // Prepopulate with current email
            model.addAttribute("emailForm", emailForm);
        }
        return "User/editEmail";
    }

    @PostMapping
    public String changeEmail(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @ModelAttribute @Valid ChangeEmailRequest emailForm,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "User/editEmail";
        }

        Long userId = currentUser.getId();
        Result<VoidResult> changeEmailResult = userManipulatingService.changeUserEmailById(userId, emailForm.getNewEmail());

        if (changeEmailResult.isFailure()) {
            model.addAttribute("error", changeEmailResult.getException().getMessage());
            model.addAttribute("emailForm", emailForm); // Ensure the form is added back to the model
            return "User/editEmail";
        }

        model.addAttribute("success", "Email updated successfully!");
        return "redirect:/user/profile?userId=" + userId;
    }
}
