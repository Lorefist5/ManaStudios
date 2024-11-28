package com.manastudio.Features.Movies.Controllers;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.manastudio.Features.Movies.Dtos.Create.CreateMovieRequestDto;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;

@Controller
public class CreateMovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies/create")
    public String showCreateMovieForm(Model model) {
        model.addAttribute("movie", new CreateMovieRequestDto()); // Add DTO for form binding
        return "Movies/create";
    }

    @PostMapping("/movies/create")
    public String createMovie(
            @Valid @ModelAttribute("movie") CreateMovieRequestDto createMovieRequestDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "Movies/create";
        }

        Movie movie = new Movie();
        movie.setTitle(createMovieRequestDto.getTitle());
        movie.setGenre(createMovieRequestDto.getGenre());
        movie.setPlot(createMovieRequestDto.getPlot());
        movie.setDirector(createMovieRequestDto.getDirector());
        movie.setActors(createMovieRequestDto.getActors());
        movie.setDateCreated(LocalDateTime.now());
        movieRepository.save(movie);

        return "redirect:/movies";
    }
}
