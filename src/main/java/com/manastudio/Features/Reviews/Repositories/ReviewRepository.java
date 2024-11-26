package com.manastudio.Features.Reviews.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manastudio.Features.Reviews.Models.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Custom query methods can be added here
}