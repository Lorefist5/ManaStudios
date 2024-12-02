package com.manastudio.Features.Reviews.Dtos.Edit;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EditReviewRequestDto {
	private Long id;
    @NotBlank(message = "Summary cannot be blank.")
    private String summary;

    @Min(value = 1, message = "Rating must be between 1 and 5.")
    @Max(value = 5, message = "Rating must be between 1 and 5.")
    private int rating;
}