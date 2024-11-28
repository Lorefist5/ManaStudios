package com.manastudio.Features.Reviews.Exceptions;

public class UserHasReviewedThisMovieException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserHasReviewedThisMovieException(String message) {
        super(message);
    }
}