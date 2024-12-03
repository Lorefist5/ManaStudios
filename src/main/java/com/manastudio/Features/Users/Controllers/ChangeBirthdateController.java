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
import com.manastudio.Features.Users.Dtos.Edit.ChangeBirthdateRequest;
import com.manastudio.Features.Users.Services.CustomUserDetails;
import com.manastudio.Features.Users.Services.UserManipulatingService;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/user/edit/birthdate")
public class ChangeBirthdateController {

    @Autowired
    private UserManipulatingService userManipulatingService;

    @GetMapping
    public String showChangeBirthdateForm(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        if (!model.containsAttribute("birthdateForm")) {
            ChangeBirthdateRequest birthdateForm = new ChangeBirthdateRequest();
            birthdateForm.setNewBirthdate(currentUser.getDateOfBirth()); // Prepopulate with current birthdate
            model.addAttribute("birthdateForm", birthdateForm);
        }
        return "User/editBirthdate";
    }

    @PostMapping
    public String changeBirthdate(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @ModelAttribute @Valid ChangeBirthdateRequest birthdateForm,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "User/editBirthdate";
        }

        Long userId = currentUser.getId();
        Result<VoidResult> changeBirthdateResult = userManipulatingService.changeUserBirthdateById(userId, birthdateForm.getNewBirthdate());

        if (changeBirthdateResult.isFailure()) {
            model.addAttribute("error", changeBirthdateResult.getException().getMessage());
            model.addAttribute("birthdateForm", birthdateForm); // Ensure the form is added back to the model
            return "User/editBirthdate";
        }

        model.addAttribute("success", "Birthdate updated successfully!");
        return "redirect:/user/profile?userId=" + userId;
    }
}
