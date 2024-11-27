package com.manastudio.Features.Movies.Controllers;

import com.manastudio.Features.Movies.Dtos.Fetch.GetMovieListResponseDto;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;
import com.manastudio.Features.Reviews.Services.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class GetMovieListController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/movies")
    public String getMovieList(Model model) {
        // Fetch all movies from the repository
        List<Movie> movies = movieRepository.findAll();

        // Map movies to response DTOs
        List<GetMovieListResponseDto> movieDtos = movies.stream()
            .map(movie -> {
                GetMovieListResponseDto dto = new GetMovieListResponseDto();
                dto.setId(movie.getId());
                dto.setTitle(movie.getTitle());
                dto.setGenre(movie.getGenre());
                dto.setPlot(movie.getPlot());
                dto.setDirector(movie.getDirector());
                dto.setActors(movie.getActors());
                // Fetch review count for the movie
                int reviewCount = (int) reviewRepository.countByMovieId(movie.getId());
                dto.setReviewCount(reviewCount);
                return dto;
            })
            .collect(Collectors.toList());

        // Add the DTOs to the model
        model.addAttribute("movies", movieDtos);

        // Return the template name (e.g., "Movies/list.html")
        return "Movies/list";
    }
}
