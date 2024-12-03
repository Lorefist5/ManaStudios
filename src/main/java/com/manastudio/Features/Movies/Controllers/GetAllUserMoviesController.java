package com.manastudio.Features.Movies.Controllers;

import com.manastudio.Features.Movies.Services.MovieFetchingService;
import com.manastudio.Features.Movies.Dtos.Fetch.MovieWithReviewsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GetAllUserMoviesController {

    @Autowired
    private MovieFetchingService movieFetchingService;

    @GetMapping("/movies/list")
    public String getAllMoviesByUser(@RequestParam("userId") Long userId, Model model) {
        // Fetch movies created by the user
        List<MovieWithReviewsDto> userMovies = movieFetchingService.fetchMoviesWithReviews()
                .stream()
                .filter(movie -> movie.getCreatedBy().getId().equals(userId))
                .toList();

        // Add user movies to the model
        model.addAttribute("movies", userMovies != null ? userMovies : List.of());
        model.addAttribute("userId", userId);

        return "Movies/userMoviesList";
    }
}