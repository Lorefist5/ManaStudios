package com.manastudio.Features.Movies.Dtos.Fetch;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GetMovieWithRatingDto {
    private Long id;
    private String title;
    private String genre;
    private String plot;
    private String director;
    private String actors;
    private double averageRating; // Average rating of the movie
    private int reviewCount; // Total number of reviews for the movie
    private String createdBy; // Username or full name of the user who created the movie
    private LocalDateTime publishedDate; // Date and time the movie was published
}
