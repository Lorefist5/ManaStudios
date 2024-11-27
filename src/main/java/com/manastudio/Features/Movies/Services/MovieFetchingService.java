package com.manastudio.Features.Movies.Services;

import com.manastudio.Features.Movies.Dtos.Fetch.GetMovieWithRatingDto;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;
import com.manastudio.Features.Reviews.Models.Review;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieFetchingService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    /**
     * Fetches the latest movies created, sorted by creation date (newest first).
     * @param count Number of movies to fetch.
     * @return List of the latest movies.
     */
    public List<Movie> fetchLatestMovies(int count) {
        return movieRepository.findAll(PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "dateCreated"))).getContent();
    }

    /**
     * Fetches the top-rated movies based on their average rating.
     * @param count Number of movies to fetch.
     * @return List of top-rated movies.
     */
    public List<GetMovieWithRatingDto> fetchTopRatedMovies(int count) {
        // Fetch movies with a limit
        List<Movie> movies = movieRepository.findAll(PageRequest.of(0, count)).getContent();

        return movies.stream()
            .map(movie -> {
                // Fetch average rating from ReviewRepository
                Double averageRating = reviewRepository.findAverageRatingByMovieId(movie.getId());
                int reviewCount = (int) reviewRepository.countByMovieId(movie.getId());

                // Map Movie and ratings to GetMovieWithRatingDto
                GetMovieWithRatingDto dto = new GetMovieWithRatingDto();
                dto.setId(movie.getId());
                dto.setTitle(movie.getTitle());
                dto.setGenre(movie.getGenre());
                dto.setPlot(movie.getPlot());
                dto.setDirector(movie.getDirector());
                dto.setActors(movie.getActors());
                dto.setAverageRating(averageRating != null ? averageRating : 0); // Handle null rating
                dto.setReviewCount(reviewCount);

                return dto;
            })
            // Sort by average rating descending
            .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
            .limit(count) // Limit to the top N movies
            .collect(Collectors.toList());
    }

}