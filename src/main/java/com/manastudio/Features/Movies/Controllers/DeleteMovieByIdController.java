package com.manastudio.Features.Movies.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.manastudio.Abstractions.Result;
import com.manastudio.Abstractions.VoidResult;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Services.MovieDeletionService;
import com.manastudio.Features.Movies.Services.MovieFetchingService;
import com.manastudio.Features.Users.Services.CustomUserDetails;

@Controller
public class DeleteMovieByIdController {

    @Autowired
    private MovieDeletionService movieDeletionService;

    @Autowired
    private MovieFetchingService movieFetchingService;

    @PostMapping("/movies/delete/{movieId}")
    public String deleteMovieById(@PathVariable Long movieId,@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        // Ensure the user is authenticated
        if (principal == null) {
            model.addAttribute("error", "You must be logged in to delete a movie.");
            return "Common/error"; // Point to your error page
        }

        // Fetch the movie to check ownership
        Result<Movie> movieResult = movieFetchingService.fetchMovieById(movieId);
        if (movieResult.isFailure()) {
            model.addAttribute("error", "Movie not found: " + movieResult.getException().getMessage());
            return "Common/error";
        }

        Movie movie = movieResult.getValue();

        // Check if the authenticated user is the creator of the movie
        Long currentUserId = principal.getId();

        if (!(movie.getCreatedBy().getId() == currentUserId)) {
            model.addAttribute("error", "You are not authorized to delete this movie.");
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