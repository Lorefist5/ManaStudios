package com.manastudio.Features.Reviews.Dtos.Create;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CreateMovieReviewRequestDto {
    @NotBlank(message = "Summary cannot be blank.")
    @Size(min = 10, max = 500, message = "Summary must be between 10 and 500 characters.")
    private String summary;

    @Min(value = 0, message = "Rating must be at least 0.")
    @Max(value = 5, message = "Rating cannot be greater than 5.")
    private int rating; // From 0 to 5
}
