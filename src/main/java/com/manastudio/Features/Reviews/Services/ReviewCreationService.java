package com.manastudio.Features.Reviews.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;
import com.manastudio.Features.Movies.Services.MovieFetchingService;
import com.manastudio.Features.Reviews.Dtos.Create.CreateMovieReviewRequestDto;
import com.manastudio.Features.Reviews.Exceptions.UserHasReviewedThisMovieException;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Repositories.UserRepository;
import com.manastudio.Features.Users.Services.UserFetchingService;

@Service
public class ReviewCreationService {

	@Autowired
    private ReviewRepository reviewRepository;
	@Autowired
    private UserFetchingService userFetchingService;
	@Autowired
    private MovieFetchingService movieFetchingService;


    public Double getAverageRatingForMovie(Long movieId) {
        // If no reviews exist, return 0
        return reviewRepository.findAverageRatingByMovieId(movieId) != null 
               ? reviewRepository.findAverageRatingByMovieId(movieId) 
               : 0.0;
    }
    public double getStarPercentage(int starRating, Long movieId) {
        long totalReviews = reviewRepository.countByMovieId(movieId);
        if (totalReviews == 0) {
            return 0.0;
        }
        long countForStar = reviewRepository.countByMovieIdAndRating(movieId, starRating);
        return (double) countForStar / totalReviews * 100;
    }
    
    public Result<Long> createMovieReview(Long userId, Long movieId, CreateMovieReviewRequestDto request) {
    	
        // Fetch the user
        Result<User> userFetchedResults = userFetchingService.fetchUserById(userId);
        if (userFetchedResults.isFailure()) {
            return Result.from(userFetchedResults.getException());
        }
        // Fetch the movie
        Result<Movie> movieFetchedResults = movieFetchingService.fetchMovieById(movieId);
        if (movieFetchedResults.isFailure()) {
            return Result.from(movieFetchedResults.getException());
        }

        // Extract user and movie objects
        User userFetchedValue = userFetchedResults.getValue();
        Movie movieFetchedValue = movieFetchedResults.getValue();

        // Check if the user has already reviewed the movie
        long existingReviewsCount = reviewRepository.countByUserIdAndMovieId(userId, movieId);
        if (existingReviewsCount > 0) {
            return Result.from(new UserHasReviewedThisMovieException("User has already reviewed this movie."));
        }

        // Create the review
        Review review = new Review();
        review.setSummary(request.getSummary());
        review.setRating(request.getRating());
        review.setUser(userFetchedValue);
        review.setMovie(movieFetchedValue);

        // Save the review and return the ID
        Review savedReview = reviewRepository.save(review);
        return Result.from(savedReview.getId());
    }
}