package com.yobel.rickandmortyback.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * <p>
 * This exception is used throughout the application to indicate that a
 * resource requested by the client (such as a character, location, or episode)
 * does not exist or could not be found in the Rick and Morty API.
 * </p>
 * <p>
 * It extends RuntimeException, making it an unchecked exception that does not
 * require explicit declaration in method signatures.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}