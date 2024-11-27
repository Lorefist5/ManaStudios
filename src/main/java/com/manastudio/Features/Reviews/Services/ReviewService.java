package com.manastudio.Features.Reviews.Services;

import org.springframework.stereotype.Service;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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
}