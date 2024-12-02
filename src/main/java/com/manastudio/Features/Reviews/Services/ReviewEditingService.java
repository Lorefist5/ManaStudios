package com.manastudio.Features.Reviews.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Reviews.Dtos.Edit.EditReviewRequestDto;
import com.manastudio.Features.Reviews.Exceptions.ReviewNotFoundException;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;

@Service
public class ReviewEditingService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Result<Review> editReview(Long reviewId, EditReviewRequestDto dto) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            return Result.failure(new ReviewNotFoundException("Review with ID " + reviewId + " not found."));
        }

        Review review = optionalReview.get();
        review.setSummary(dto.getSummary());
        review.setRating(dto.getRating());

        Review updatedReview = reviewRepository.save(review);
        return Result.success(updatedReview);
    }
}
