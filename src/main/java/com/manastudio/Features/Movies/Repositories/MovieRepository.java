package com.manastudio.Features.Movies.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manastudio.Features.Movies.Models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Custom query methods can be added here
	List<Movie> findByTitleContainingIgnoreCase(String title);
}