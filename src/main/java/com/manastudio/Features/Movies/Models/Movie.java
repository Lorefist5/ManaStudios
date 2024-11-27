package com.manastudio.Features.Movies.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String genre; // E.g., Comedy, Action, Suspense

    @Getter
    @Setter
    private String plot;

    @Getter
    @Setter
    private String director;

    @Getter
    @Setter
    private String actors; // A comma-separated list of actors
    
    @CreatedDate
    @Getter
    @Setter
    private LocalDateTime dateCreated;
}