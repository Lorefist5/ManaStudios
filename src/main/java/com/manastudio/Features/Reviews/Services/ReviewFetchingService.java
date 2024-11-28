package com.manastudio.Features.Reviews.Services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manastudio.Features.Movies.Dtos.Fetch.StarPercentageDto;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;

@Service
public class ReviewFetchingService {
	
	@Autowired
    private ReviewRepository reviewRepository;


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
    
}
