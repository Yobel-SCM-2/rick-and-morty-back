package com.yobel.rickandmortyback.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a standardized API error response.
 * <p>
 * This class encapsulates error information that is returned to API clients
 * when an exception occurs. It includes the HTTP status code, a descriptive
 * message, and a timestamp of when the error occurred.
 * </p>
 * <p>
 * This class uses Lombok annotations to automatically generate boilerplate code:
 * </p>
 * <ul>
 *   <li>@Data - Generates getters, setters, toString, equals, and hashCode methods</li>
 *   <li>@AllArgsConstructor - Generates a constructor with all fields as parameters</li>
 *   <li>@NoArgsConstructor - Generates a no-args constructor</li>
 * </ul>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    /**
     * The HTTP status code associated with this error.
     */
    private int status;

    /**
     * A descriptive error message providing details about what went wrong.
     */
    private String message;

    /**
     * The timestamp indicating when the error occurred.
     */
    private LocalDateTime timestamp;
}