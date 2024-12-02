package com.manastudio.Configs;


import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seedData() {
        if (userRepository.count() > 0 || movieRepository.count() > 0 || reviewRepository.count() > 0) {
            return;
        }

        // Create Users
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setUsername("johndoe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setDateOfBirth(LocalDate.of(1990, 1, 15));

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setUsername("janesmith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword(passwordEncoder.encode("securepassword"));
        user2.setDateOfBirth(LocalDate.of(1985, 5, 20));

        userRepository.saveAll(Arrays.asList(user1, user2));

        // Create Movies
        Movie movie1 = new Movie();
        movie1.setTitle("Inception");
        movie1.setGenre("Sci-Fi");
        movie1.setPlot("A thief who steals secrets through dreams is given a chance at redemption.");
        movie1.setDirector("Christopher Nolan");
        movie1.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        movie1.setDateCreated(LocalDateTime.now());
        movie1.setPublishedDate(LocalDateTime.now());
        movie1.setCreatedBy(user1);

        Movie movie2 = new Movie();
        movie2.setTitle("The Matrix");
        movie2.setGenre("Action");
        movie2.setPlot("A hacker discovers the reality he lives in is a simulation.");
        movie2.setDirector("The Wachowskis");
        movie2.setActors("Keanu Reeves, Laurence Fishburne");
        movie2.setDateCreated(LocalDateTime.now());
        movie2.setPublishedDate(LocalDateTime.now());
        movie2.setCreatedBy(user2);

        movieRepository.saveAll(Arrays.asList(movie1, movie2));

        // Create Reviews
        Review review1 = new Review();
        review1.setSummary("Mind-bending and visually stunning!");
        review1.setRating(5);
        review1.setUser(user1);
        review1.setMovie(movie1);

        Review review2 = new Review();
        review2.setSummary("A must-watch sci-fi masterpiece.");
        review2.setRating(5);
        review2.setUser(user2);
        review2.setMovie(movie2);

        reviewRepository.saveAll(Arrays.asList(review1, review2));
    }
}
