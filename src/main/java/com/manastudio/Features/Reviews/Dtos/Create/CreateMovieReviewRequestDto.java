package com.manastudio.Features.Reviews.Dtos.Create;
import lombok.Data;

@Data
public class CreateMovieReviewRequestDto {
    private String summary;
    private int rating; // From 0 to 5
}
