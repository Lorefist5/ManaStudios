package com.manastudio.Features.Reviews.Services;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import com.manastudio.Abstractions.Result;
import com.manastudio.Abstractions.VoidResult;
import com.manastudio.Features.Reviews.Exceptions.ReviewNotFoundException;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;

@Service
public class ReviewDeletionService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Result<VoidResult> deleteReviewById(Long reviewId) {
        // Check if the review exists
        if (!reviewRepository.existsById(reviewId)) {
            return Result.failure(new ReviewNotFoundException("Review with ID " + reviewId + " not found."));
        }

        try {
            // Delete the review
            reviewRepository.deleteById(reviewId);
            return Result.success(VoidResult.create());
        } catch (Exception e) {
            return Result.failure(e);
        }
    }
}