package com.manastudio.Features.Movies.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.manastudio.Abstractions.Result;
import com.manastudio.Abstractions.VoidResult;
import com.manastudio.Features.Movies.Services.MovieDeletionService;

@Controller
public class DeleteMovieByIdController {

    @Autowired
    private MovieDeletionService movieDeletionService;

    @PostMapping("/movies/delete/{movieId}")
    public String deleteMovieById(@PathVariable Long movieId, Model model, Authentication authentication) {
        // Ensure the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("error", "You must be logged in to delete a movie.");
            return "Common/error"; // Point to your error page
        }

        // Attempt to delete the movie
        Result<VoidResult> deletionResult = movieDeletionService.deleteMovieById(movieId);

        // Check if the deletion was successful
        if (deletionResult.isFailure()) {
            // Add error message to the model for display
            model.addAttribute("error", "Failed to delete movie: " + deletionResult.getException().getMessage());
            return "Common/error"; // Point to your error page
        }

        // Redirect to the movies list page upon successful deletion
        return "redirect:/movies";
    }
}
