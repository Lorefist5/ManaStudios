package com.manastudio.Features.Reviews.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Movies.Dtos.Fetch.MovieWithReviewsDto;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;
import com.manastudio.Features.Movies.Services.MovieFetchingService;
import com.manastudio.Features.Reviews.Dtos.Create.CreateMovieReviewRequestDto;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;
import com.manastudio.Features.Reviews.Services.ReviewCreationService;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Services.CustomUserDetails;
import com.manastudio.Features.Users.Services.UserAuthorizationService;

import jakarta.validation.Valid;


@Controller
public class CreateMovieReviewController {
	
    @Autowired
    private MovieFetchingService movieFetchingService;
    @Autowired
    private ReviewCreationService reviewCreationService;

	
    @GetMapping("/movies/{movieId}/review")
    public String showReviewForm(
            @PathVariable Long movieId, 
            @AuthenticationPrincipal CustomUserDetails currentUser, 
            Model model) {
        Result<MovieWithReviewsDto> fetchedMovieResults = movieFetchingService.fetchMovieWithReviews(movieId);

        if (fetchedMovieResults.isFailure()) {
            model.addAttribute("error", "Movie not found.");
            return "Common/error"; // Use an error page if the movie isn't found.
        }

        MovieWithReviewsDto movie = fetchedMovieResults.getValue();
        boolean alreadyReviewed = movie.getReviews().stream()
                .anyMatch(review -> review.getUser().getId().equals(currentUser.getId()));
        if (alreadyReviewed) {
            // Fetch the list of movies to populate the model for Movies/list
            List<MovieWithReviewsDto> movies = movieFetchingService.fetchMoviesWithReviews();
            model.addAttribute("movies", movies);
            model.addAttribute("error", "You have already reviewed this movie.");
            return "Movies/list"; // Redirect back to the movies list with an error message
        }

        // Add movie and empty review DTO to the model
        model.addAttribute("movie", movie);
        model.addAttribute("review", new CreateMovieReviewRequestDto());

        return "Reviews/create"; // Path to the review creation form
    }

    
    @PostMapping("/movies/{movieId}/review")
    public String createReview(
            @PathVariable Long movieId,
            @Valid @ModelAttribute("review") CreateMovieReviewRequestDto createReviewDto,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Model model) {

        // Handle validation errors
        if (result.hasErrors()) {
            // Fetch the movie details for redisplaying the form
            Result<MovieWithReviewsDto> movieResult = movieFetchingService.fetchMovieWithReviews(movieId);
            if (movieResult.isFailure()) {
                model.addAttribute("error", "Movie not found.");
                return "Common/error";
            }
            model.addAttribute("movie", movieResult.getValue());
            return "Reviews/create"; // Redisplay form with error messages
        }

        // Attempt to create the review
        Result<Long> reviewCreationResults = reviewCreationService.createMovieReview(currentUser.getId(), movieId, createReviewDto);
        if (reviewCreationResults.isFailure()) {
            // Fetch the movie details for redisplaying the form
            Result<MovieWithReviewsDto> movieResult = movieFetchingService.fetchMovieWithReviews(movieId);
            if (movieResult.isFailure()) {
                model.addAttribute("error", "Movie not found.");
                return "Common/error";
            }
            model.addAttribute("movie", movieResult.getValue());
            model.addAttribute("error", reviewCreationResults.getException().getMessage());
            return "Reviews/create"; // Redisplay form with error messages
        }

        // Redirect to the movie page after successful review creation
        return "redirect:/movies/" + movieId;
    }


}
