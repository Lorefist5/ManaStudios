package com.manastudio.Features.Reviews.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Reviews.Dtos.Edit.EditReviewRequestDto;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Services.ReviewEditingService;
import com.manastudio.Features.Reviews.Services.ReviewFetchingService;

import jakarta.validation.Valid;

@Controller
public class EditMovieReviewController {

    @Autowired
    private ReviewFetchingService reviewFetchingService;

    @Autowired
    private ReviewEditingService reviewEditingService;

    @GetMapping("/reviews/edit/{reviewId}")
    public String showEditForm(@PathVariable Long reviewId, Model model) {
        Result<Review> fetchResult = reviewFetchingService.fetchReviewById(reviewId);

        if (fetchResult.isFailure()) {
            model.addAttribute("error", fetchResult.getException().getMessage());
            return "Common/error";
        }

        Review review = fetchResult.getValue();

        // Populate DTO with the current review data
        EditReviewRequestDto dto = new EditReviewRequestDto();
        dto.setId(review.getId());
        dto.setSummary(review.getSummary());
        dto.setRating(review.getRating());

        model.addAttribute("review", dto);
        model.addAttribute("reviewId", reviewId);
        return "Reviews/edit";
    }

    @PostMapping("/reviews/edit/{reviewId}")
    public String editReview(
            @PathVariable Long reviewId,
            @Valid @ModelAttribute("review") EditReviewRequestDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("reviewId", reviewId);
            return "Reviews/edit";
        }

        Result<Review> editResult = reviewEditingService.editReview(reviewId, dto);

        if (editResult.isFailure()) {
            model.addAttribute("error", editResult.getException().getMessage());
            return "Common/error";
        }

        return "redirect:/reviews/list?userId=" + editResult.getValue().getUser().getId();
    }
}
