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
    public String getMoviesWithReviews(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "rating", required = false) Double rating,
            @RequestParam(value = "query", required = false) String query,
            Model model) {

        // Fetch genres
        List<String> genres = movieFetchingService.fetchAllCategories();
        model.addAttribute("genres", genres);

        // Fetch movies
        List<MovieWithReviewsDto> allMovies = movieFetchingService.fetchMoviesWithReviews();
        List<MovieWithReviewsDto> filteredMovies = allMovies;

        // Apply search filter
        if (query != null && !query.isEmpty()) {
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Apply genre filter
        if (genre != null && !genre.isEmpty()) {
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        // Apply rating filter
        if (rating != null) {
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> movie.getAverageRating() >= rating)
                    .collect(Collectors.toList());
        }

        // Pagination logic
        int totalMovies = filteredMovies.size();
        int totalPages = (int) Math.ceil((double) totalMovies / size);
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalMovies);
        List<MovieWithReviewsDto> paginatedMovies = filteredMovies.subList(startIndex, endIndex);

        // Add attributes to the model
        model.addAttribute("movies", paginatedMovies);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalMovies", totalMovies);

        // Add active filters
        model.addAttribute("activeQuery", query);
        model.addAttribute("activeGenre", genre);
        model.addAttribute("activeRating", rating);

        return "Movies/list";
    }

}