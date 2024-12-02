package com.manastudio.Features.Reviews.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Movies.Dtos.Fetch.StarPercentageDto;
import com.manastudio.Features.Reviews.Exceptions.ReviewNotFoundException;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;
import com.manastudio.Features.Users.Models.User;
import com.manastudio.Features.Users.Services.UserFetchingService;

@Service
public class ReviewFetchingService {
	
	@Autowired
    private ReviewRepository reviewRepository;
	@Autowired
	private UserFetchingService userFetchingService;
    public Result<Review> fetchReviewById(Long reviewId) {
    	
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            return Result.failure(new ReviewNotFoundException("Review with ID " + reviewId + " not found."));
        }

        return Result.success(optionalReview.get());
    }
    
    public List<StarPercentageDto> calculateStarPercentages(Long movieId) {
        long totalReviews = reviewRepository.countByMovieId(movieId);
        if (totalReviews == 0) {
            // Return a list with 0% for all stars if no reviews exist
            return IntStream.rangeClosed(1, 5)
                    .mapToObj(star -> new StarPercentageDto(star, 0.0))
                    .collect(Collectors.toList());
        }

        return IntStream.rangeClosed(1, 5)
                .mapToObj(star -> {
                    long countForStar = reviewRepository.countByMovieIdAndRating(movieId, star);
                    double percentage = (double) countForStar / totalReviews * 100;
                    return new StarPercentageDto(star, percentage);
                })
                .collect(Collectors.toList());
    }

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
    
    public Result<List<Review>> getReviewsByUserId(Long userId) {
    	Result<User> fetchedUserResults = userFetchingService.fetchUserById(userId);
    	if(fetchedUserResults.isFailure())
    		return Result.failure(fetchedUserResults.getException());
    	List<Review> fetchedListOfUserReviews = reviewRepository.findByUserId(userId);
        return Result.success(fetchedListOfUserReviews);
    }
    
    
}
