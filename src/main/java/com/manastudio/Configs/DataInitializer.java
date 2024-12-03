package com.manastudio.Configs;


import com.github.javafaker.Faker;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public void seedDataWithFaker() {
        if (userRepository.count() > 0 || movieRepository.count() > 0 || reviewRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();

        // Create Random Users
        List<User> users = new ArrayList();
        for (int i = 0; i < 40; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setUsername(faker.internet().emailAddress());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode("password" + i));
            user.setDateOfBirth(LocalDate.now().minusYears(20));
            users.add(user);
        }
        userRepository.saveAll(users);

        // Create Random Movies
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movie.setTitle(faker.book().title());
            movie.setGenre(faker.options().option("Action", "Comedy", "Drama", "Horror", "Sci-Fi"));
            movie.setPlot(faker.lorem().sentence(15));
            movie.setDirector(faker.name().fullName());
            movie.setActors(faker.name().fullName() + ", " + faker.name().fullName());
            movie.setDateCreated(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 100)));
            movie.setPublishedDate(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 50)));
            movie.setCreatedBy(users.get(faker.number().numberBetween(0, users.size())));
            movies.add(movie);
        }
        movieRepository.saveAll(movies);

        // Create Random Reviews
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Review review = new Review();
            review.setSummary(faker.lorem().sentence(10));
            review.setRating(faker.number().numberBetween(1, 5));
            review.setUser(users.get(faker.number().numberBetween(0, users.size())));
            review.setMovie(movies.get(faker.number().numberBetween(0, movies.size())));
            reviews.add(review);
        }
        reviewRepository.saveAll(reviews);
    }
}
