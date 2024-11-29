package com.manastudio.Features.Movies.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manastudio.Abstractions.Result;
import com.manastudio.Abstractions.VoidResult;
import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Movies.Repositories.MovieRepository;
import com.manastudio.Features.Reviews.Repositories.ReviewRepository;

import org.springframework.transaction.annotation.Transactional;

@Service	
public class MovieDeletionService {

	@Autowired
	private MovieFetchingService movieFetchingService;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	
	
	@Transactional 
	public Result<VoidResult> deleteMovieById(Long movieId) {
		
		Result<Movie> fetchedMovieResults = movieFetchingService.fetchMovieById(movieId);
		if(fetchedMovieResults.isFailure())//
			return Result.failure(fetchedMovieResults.getException());
		
		reviewRepository.deleteByMovieId(movieId);
        movieRepository.deleteById(movieId);
		return Result.from(VoidResult.create());
	}
	
}
