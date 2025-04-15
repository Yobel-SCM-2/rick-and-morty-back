package com.yobel.rickandmortyback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Global exception handler for the Rick and Morty backend application.
 * <p>
 * This class provides centralized exception handling across all controllers
 * in the application. It translates various exceptions into appropriate HTTP
 * responses with standardized error payloads.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns a NOT_FOUND response.
     *
     * @param ex The ResourceNotFoundException that was thrown
     * @return A Mono containing a ResponseEntity with ApiError details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ApiError>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError));
    }

    /**
     * Handles WebClientResponseException and returns an appropriate response based on the error.
     * <p>
     * This method customizes error messages based on the HTTP status code
     * from the external API response.
     * </p>
     *
     * @param ex The WebClientResponseException that was thrown
     * @return A Mono containing a ResponseEntity with ApiError details
     */
    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<ApiError>> handleWebClientResponseException(WebClientResponseException ex) {
        String message = "Error communicating with external API";

        // Customize message based on status code
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            message = "Resource not found in external API";
        } else if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            message = "Invalid request to external API";
        } else if (ex.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            message = "Internal error in external API";
        }

        ApiError apiError = new ApiError(
                ex.getStatusCode().value(),
                message + ": " + ex.getMessage(),
                LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(apiError));
    }

    /**
     * Handles all other unhandled exceptions and returns an INTERNAL_SERVER_ERROR response.
     * <p>
     * This is a fallback handler for any exceptions not specifically handled
     * by other exception handlers.
     * </p>
     *
     * @param ex The Exception that was thrown
     * @return A Mono containing a ResponseEntity with ApiError details
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiError>> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError));
    }
}