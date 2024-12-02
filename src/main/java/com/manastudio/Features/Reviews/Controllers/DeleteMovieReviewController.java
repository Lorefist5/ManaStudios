package com.manastudio.Features.Reviews.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.manastudio.Abstractions.Result;
import com.manastudio.Abstractions.VoidResult;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Services.ReviewDeletionService;
import com.manastudio.Features.Reviews.Services.ReviewFetchingService;
import com.manastudio.Features.Users.Services.CustomUserDetails;

@Controller
public class DeleteMovieReviewController {
    @Autowired
    private ReviewDeletionService reviewDeletionService;

    @Autowired
    private ReviewFetchingService reviewFetchingService;

    @PostMapping("/reviews/delete/{reviewId}")
    public String deleteReviewById(@PathVariable Long reviewId, @AuthenticationPrincipal CustomUserDetails principal, Model model) {
        // Ensure the user is authenticated
        if (principal == null) {
            model.addAttribute("error", "You must be logged in to delete a review.");
            return "Common/error";
        }

        // Fetch the review to check ownership
        Result<Review> reviewResult = reviewFetchingService.fetchReviewById(reviewId);
        if (reviewResult.isFailure()) {
            model.addAttribute("error", "Review not found: " + reviewResult.getException().getMessage());
            return "Common/error";
        }

        Review review = reviewResult.getValue();

        // Check if the authenticated user is the creator of the review
        Long currentUserId = principal.getId();
        if (!review.getUser().getId().equals(currentUserId)) {
            model.addAttribute("error", "You are not authorized to delete this review.");
            return "Common/error";
        }

        // Attempt to delete the review
        Result<VoidResult> deletionResult = reviewDeletionService.deleteReviewById(reviewId);

        // Check if the deletion was successful
        if (deletionResult.isFailure()) {
            model.addAttribute("error", "Failed to delete review: " + deletionResult.getException().getMessage());
            return "Common/error";
        }

        // Redirect to the user's reviews page upon successful deletion
        return "redirect:/reviews/list?userId=" + currentUserId;
    }
	
}
