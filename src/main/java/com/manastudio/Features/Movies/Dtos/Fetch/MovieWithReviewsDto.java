package com.manastudio.Features.Movies.Dtos.Fetch;



import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Users.Models.User;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieWithReviewsDto {
    private Long id;
    private String title;
    private String genre;
    private String plot;
    private String director;
    private String actors;
    private LocalDateTime dateCreated;
    private LocalDateTime publishedDate;
    private User createdBy; // Change this to a User object
    private List<Review> reviews;

    public int getReviewCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0; // Default to 0 if there are no reviews
        }
        return reviews.stream()
                      .mapToDouble(Review::getRating) // Assuming Review has a `getRating` method
                      .average()
                      .orElse(0.0);
    }
}
