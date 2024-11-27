package com.manastudio.Features.Movies.Dtos.Create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMovieRequestDto {

    @NotBlank(message = "Title is required.")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters.")
    private String title;

    @NotBlank(message = "Genre is required.")
    @Size(min = 3, max = 50, message = "Genre must be between 3 and 50 characters.")
    private String genre;

    @NotBlank(message = "Plot is required.")
    @Size(min = 10, max = 500, message = "Plot must be between 10 and 500 characters.")
    private String plot;

    @NotBlank(message = "Director is required.")
    @Size(min = 2, max = 100, message = "Director name must be between 2 and 100 characters.")
    private String director;

    @NotBlank(message = "Actors are required.")
    @Size(min = 5, max = 300, message = "Actors list must be between 5 and 300 characters.")
    private String actors;
}