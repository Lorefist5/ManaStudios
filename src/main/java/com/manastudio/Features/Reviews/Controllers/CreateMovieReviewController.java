package com.manastudio.Features.Reviews.Controllers;

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
    public String showReviewForm(@PathVariable Long movieId, Model model) {
        Result<Movie> movieOptional = movieFetchingService.fetchMovieById(movieId);

        if (movieOptional.isFailure()) {
            model.addAttribute("error", "Movie not found.");
            return "Common/error"; // Use an error page if the movie isn't found.
        }

        Movie movie = movieOptional.getValue();

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
            Model model) 
    {

        if (result.hasErrors()) 
        {
            model.addAttribute("error", "Invalid input.");
            return "Reviews/create";
        }
        
        Result<Long> reviewCreationResults = reviewCreationService.createMovieReview(currentUser.getId(), movieId, createReviewDto);
        if(reviewCreationResults.isFailure())
        {
        	model.addAttribute("error", reviewCreationResults.getException().getMessage());
        	return "Reviews/create";
        }
        
        
        // Redirect to the movie page or a success page
        return "redirect:/movies/" + movieId;
    }

}
