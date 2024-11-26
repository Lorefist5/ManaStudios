package com.manastudio.Features.Users.Exceptions;



public class UserPasswordIsNotSameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserPasswordIsNotSameException(String message) {
        super(message);
    }
}
