package com.manastudio.Features.Reviews.Models;



import com.manastudio.Features.Movies.Models.Movie;
import com.manastudio.Features.Users.Models.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String summary;

    @Getter
    @Setter
    private int rating; // Rating from 0 to 5

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user; // The user who created the review

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @Getter
    @Setter
    private Movie movie; // The movie being reviewed
    

}
