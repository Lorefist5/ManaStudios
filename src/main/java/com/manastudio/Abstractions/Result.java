package com.manastudio.Abstractions;

public class Result<T> {
    private final T value;
    private final Exception exception;

    // Private constructor
    private Result(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    // Static factory method for success
    public static <T> Result<T> success(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Success result must have a value.");
        }
        return new Result<>(value, null);
    }

    // Static factory method for failure
    public static <T> Result<T> failure(Exception exception) {
        if (exception == null) {
            throw new IllegalArgumentException("Failure result must have an exception.");
        }
        return new Result<>(null, exception);
    }

    // Overloaded `from` for success
    public static <T> Result<T> from(T value) {
        return success(value);
    }

    // Overloaded `from` for failure
    public static <T> Result<T> from(Exception exception) {
        return failure(exception);
    }

    // Check if the result is a success
    public boolean isSuccess() {
        return exception == null;
    }

    // Check if the result is a failure
    public boolean isFailure() {
        return exception != null;
    }

    // Get the successful value, or throw an exception if it's a failure
    public T getValue() {
        if (isFailure()) {
            throw new IllegalStateException("Cannot access value on a failed result.", exception);
        }
        return value;
    }

    // Get the exception, or throw an exception if it's a success
    public Exception getException() {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot access exception on a successful result.");
        }
        return exception;
    }
}
