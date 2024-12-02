package com.manastudio.Features.Movies.Controllers;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.manastudio.Features.Movies.Dtos.Create.CreateMovieRequestDto;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;
import com.manastudio.Features.Users.Services.CustomUserDetails;

@Controller
public class CreateMovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository; // To fetch user details

    @GetMapping("/movies/create")
    public String showCreateMovieForm(Model model) {
        model.addAttribute("movie", new CreateMovieRequestDto()); // Add DTO for form binding
        return "Movies/create";
    }

    @PostMapping("/movies/create")
    public String createMovie(
            @Valid @ModelAttribute("movie") CreateMovieRequestDto createMovieRequestDto,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal CustomUserDetails principal) { // Spring Security's authenticated user

        if (bindingResult.hasErrors()) {
            return "Movies/create";
        }

        // Fetch the current authenticated user
        User currentUser = userRepository.findByUsername(principal.getUsername());
        if (currentUser == null) {
            model.addAttribute("error", "Current user not found.");
            return "Common/error"; // Render a generic error page
        }

        // Map the DTO to the Movie entity
        Movie movie = new Movie();
        movie.setTitle(createMovieRequestDto.getTitle());
        movie.setGenre(createMovieRequestDto.getGenre());
        movie.setPlot(createMovieRequestDto.getPlot());
        movie.setDirector(createMovieRequestDto.getDirector());
        movie.setActors(createMovieRequestDto.getActors());
        movie.setDateCreated(LocalDateTime.now());
        movie.setCreatedBy(currentUser); // Set the current user as the creator
        movie.setPublishedDate(LocalDateTime.now()); // Set the published date

        // Save the movie to the database
        movieRepository.save(movie);

        return "redirect:/movies"; // Redirect to the movies list
    }
}
