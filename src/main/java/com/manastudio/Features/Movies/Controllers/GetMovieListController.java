package com.manastudio.Features.Movies.Controllers;


import com.manastudio.Features.Movies.Dtos.Fetch.MovieWithReviewsDto;
import com.manastudio.Features.Movies.Services.MovieFetchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class GetMovieListController {

    @Autowired
    private MovieFetchingService movieFetchingService;

    private List<MovieWithReviewsDto> allMovies; // Cache for all movies

    @GetMapping("/movies")
    public String getMoviesWithReviews(Model model) {
        // Fetch and cache all movies
      
    	allMovies = movieFetchingService.fetchMoviesWithReviews();
        

        model.addAttribute("movies", allMovies);
        return "Movies/list";
    }

    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        if (allMovies == null) {
            allMovies = movieFetchingService.fetchMoviesWithReviews();
        }

        // Filter movies based on the search query
        List<MovieWithReviewsDto> filteredMovies = allMovies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        model.addAttribute("movies", filteredMovies);
        return "Movies/list";
    }
}