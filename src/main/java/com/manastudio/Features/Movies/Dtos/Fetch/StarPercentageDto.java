package com.manastudio.Features.Movies.Dtos.Fetch;

import lombok.Data;

@Data
public class StarPercentageDto {
    private int starRating;
    private double percentage;

    public StarPercentageDto(int starRating, double percentage) {
        this.starRating = starRating;
        this.percentage = percentage;
    }
}