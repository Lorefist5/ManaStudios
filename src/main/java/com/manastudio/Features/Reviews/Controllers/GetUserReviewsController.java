	package com.manastudio.Features.Reviews.Controllers;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Services.ReviewFetchingService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GetUserReviewsController {

    @Autowired
    private ReviewFetchingService reviewFetchingService;

    @GetMapping("/reviews/list")
    public String getUserReviews(@RequestParam("userId") Long userId, Model model) {
        // Fetch the user's reviews
        Result<List<Review>> userReviewsFetchedResults = reviewFetchingService.getReviewsByUserId(userId);

        if (userReviewsFetchedResults.isFailure()) {
            // Handle error
            model.addAttribute("error", userReviewsFetchedResults.getException().getMessage());
            return "Common/error"; // Render a generic error page
        }

        // Add reviews to the model
        model.addAttribute("reviews", userReviewsFetchedResults.getValue());

        return "Reviews/list"; // Render the reviews page
    }
}