package com.manastudio.Features.Movies.Dtos.Fetch;

import lombok.Data;


@Data
public class GetMovieListResponseDto {
    private Long id;
    private String title;
    private String genre;
    private String plot;
    private String director;
    private String actors;
    private int reviewCount;
}
