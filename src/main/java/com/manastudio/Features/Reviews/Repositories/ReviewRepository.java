package com.manastudio.Features.Reviews.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manastudio.Features.Reviews.Models.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);
    
    // Counts all reviews for a specific movie by its ID
    long countByMovieId(Long movieId);

    // Counts all reviews for a specific movie with a specific rating
    long countByMovieIdAndRating(Long movieId, int rating);
}