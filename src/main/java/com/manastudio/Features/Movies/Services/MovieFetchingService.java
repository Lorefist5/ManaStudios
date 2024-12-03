package com.manastudio.Features.Movies.Services;

import com.manastudio.Abstractions.Result;
import com.manastudio.Features.Movies.Dtos.Fetch.GetMovieWithRatingDto;
import com.manastudio.Features.Movies.Dtos.Fetch.MovieWithReviewsDto;
import com.manastudio.Features.Movies.Exceptions.MovieNotFoundException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieFetchingService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    public List<String> fetchAllCategories() {
        return movieRepository.findAll()
                .stream()
                .map(Movie::getGenre) // Extract genres
                .distinct() // Ensure uniqueness
                .sorted(String::compareToIgnoreCase) // Sort alphabetically
                .collect(Collectors.toList());
    }
    
    public List<MovieWithReviewsDto> fetchMoviesWithReviews() {
        List<Movie> movies = movieRepository.findAll();

        return movies.stream()
            .map(movie -> {
                List<Review> reviews = reviewRepository.findByMovieId(movie.getId());
                MovieWithReviewsDto dto = new MovieWithReviewsDto();
                dto.setId(movie.getId());
                dto.setTitle(movie.getTitle());
                dto.setGenre(movie.getGenre());
                dto.setPlot(movie.getPlot());
                dto.setDirector(movie.getDirector());
                dto.setActors(movie.getActors());
                dto.setDateCreated(movie.getDateCreated());
                dto.setPublishedDate(movie.getDateCreated());
                dto.setCreatedBy(movie.getCreatedBy());// Pass the full User object here
                dto.setReviews(reviews);
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    

    public Result<MovieWithReviewsDto> fetchMovieWithReviews(Long movieId) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (!movieOptional.isPresent()) {
            return Result.from(new MovieNotFoundException("Movie not found."));
        }

        Movie movie = movieOptional.get();
        List<Review> reviews = reviewRepository.findByMovieId(movieId);

        MovieWithReviewsDto movieWithReviewsDto = new MovieWithReviewsDto();
        movieWithReviewsDto.setId(movie.getId());
        movieWithReviewsDto.setTitle(movie.getTitle());
        movieWithReviewsDto.setGenre(movie.getGenre());
        movieWithReviewsDto.setPlot(movie.getPlot());
        movieWithReviewsDto.setDirector(movie.getDirector());
        movieWithReviewsDto.setActors(movie.getActors());
        movieWithReviewsDto.setDateCreated(movie.getDateCreated());
        movieWithReviewsDto.setPublishedDate(movie.getPublishedDate());
        movieWithReviewsDto.setCreatedBy(movie.getCreatedBy()); // Fetch the username of the creator
        movieWithReviewsDto.setReviews(reviews);

        return Result.from(movieWithReviewsDto);
    }
    
    
    public Result<Movie> fetchMovieById(Long movieId){
    	Optional<Movie> movieFetchedResults = movieRepository.findById(movieId);
    	
    	if(movieFetchedResults.isPresent() == false)
    		return Result.failure(new MovieNotFoundException("Movie id not found"));
    	
    	return Result.success(movieFetchedResults.get());
    }
    /**
     * Fetches the latest movies created, sorted by creation date (newest first).
     * @param count Number of movies to fetch.
     * @return List of the latest movies.
     */
    public List<Movie> fetchLatestMovies(int count) {
        return movieRepository.findAll(PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "publishedDate"))).getContent();
    }
    /**
     * Fetches the top-rated movies based on their average rating.
     * @param count Number of movies to fetch.
     * @return List of top-rated movies.
     */
    public List<GetMovieWithRatingDto> fetchTopRatedMovies(int count) {
        List<Movie> movies = movieRepository.findAll(PageRequest.of(0, count)).getContent();

        return movies.stream()
            .map(movie -> {
                Double averageRating = reviewRepository.findAverageRatingByMovieId(movie.getId());
                int reviewCount = (int) reviewRepository.countByMovieId(movie.getId());

                GetMovieWithRatingDto dto = new GetMovieWithRatingDto();
                dto.setId(movie.getId());
                dto.setTitle(movie.getTitle());
                dto.setGenre(movie.getGenre());
                dto.setPlot(movie.getPlot());
                dto.setDirector(movie.getDirector());
                dto.setActors(movie.getActors());
                dto.setAverageRating(averageRating != null ? averageRating : 0); // Handle null rating
                dto.setReviewCount(reviewCount);
                dto.setPublishedDate(movie.getPublishedDate()); // Include published date
                dto.setCreatedBy(movie.getCreatedBy().getUsername()); // Include creator username

                return dto;
            })
            .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
            .limit(count)
            .collect(Collectors.toList());
    }


}