package com.manastudio.Features.Movies.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Movies.Dtos.Fetch.MovieWithReviewsDto;
import com.manastudio.Features.Movies.Dtos.Fetch.StarPercentageDto;
import com.manastudio.Features.Movies.Services.MovieFetchingService;
import com.manastudio.Features.Reviews.Services.ReviewFetchingService;

@Controller
public class GetMovieByIdController {

    @Autowired
    private MovieFetchingService movieFetchingService;

    @Autowired
    private ReviewFetchingService reviewFetchingService;

    @GetMapping("/movies/{movieId}")
    public String getMovieById(@PathVariable Long movieId, Model model) {
        // Fetch movie with reviews
        Result<MovieWithReviewsDto> result = movieFetchingService.fetchMovieWithReviews(movieId);

        if (result.isFailure()) {
            model.addAttribute("error", result.getException().getMessage());
            return "Common/error";
        }

        MovieWithReviewsDto movie = result.getValue();

        // Fetch average rating
        double averageRating = reviewFetchingService.getAverageRatingForMovie(movieId);

        // Create list of star percentages
        List<StarPercentageDto> starPercentages = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            double percentage = reviewFetchingService.getStarPercentage(i, movieId);
            starPercentages.add(new StarPercentageDto(i, percentage));
        }

        // Add attributes to the model
        model.addAttribute("movie", movie);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("starPercentages", starPercentages);
        model.addAttribute("reviews", movie.getReviews());

        return "Movies/details";
    }
}