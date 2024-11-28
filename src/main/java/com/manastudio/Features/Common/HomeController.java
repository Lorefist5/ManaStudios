package com.manastudio.Features.Common;


import com.manastudio.Features.Movies.Dtos.Fetch.GetMovieWithRatingDto;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Services.MovieFetchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MovieFetchingService movieFetchingService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        // Fetch the latest movies
        List<Movie> latestMovies = movieFetchingService.fetchLatestMovies(10);

        // Fetch the top-rated movies
        List<GetMovieWithRatingDto> topRatedMovies = movieFetchingService.fetchTopRatedMovies(10);

        // Add movies to the model
        model.addAttribute("latestMovies", latestMovies);
        model.addAttribute("topRatedMovies", topRatedMovies);

        return "Common/home"; // This will map to the home.html template
    }
}