package com.yobel.rickandmortyback.service;

import com.yobel.rickandmortyback.exception.ResourceNotFoundException;
import com.yobel.rickandmortyback.model.response.CharacterResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import com.yobel.rickandmortyback.model.Character;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Service for handling character-related operations with the Rick and Morty API.
 * <p>
 * This service provides methods to interact with the character endpoints of the Rick and Morty API.
 * It uses WebClient to make reactive HTTP requests and handle responses in a non-blocking way.
 * All methods incorporate error handling, request timeouts, and appropriate transformations of API responses.
 * </p>
 */
@Service
@Log4j2
public class CharacterService {
    private final WebClient webClient;
    /**
     * Path to the character endpoint in the Rick and Morty API
     */
    private static final String CHARACTER_API_PATH = "/character";
    /**
     * Timeout duration for API requests
     */
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Constructs a new CharacterService with the provided WebClient.
     *
     * @param webClient The WebClient to use for making API requests
     */
    public CharacterService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Retrieves all characters from the Rick and Morty API.
     *
     * @return A Flux of Character objects containing all available characters
     */
    public Flux<Character> getAllCharacters() {
        return webClient.get()
                .uri(CHARACTER_API_PATH)
                .retrieve()
                .bodyToMono(CharacterResponse.class)
                .timeout(REQUEST_TIMEOUT)
                .onErrorMap(this::handleApiError)
                .flatMapMany(response -> Flux.fromIterable(response.getResults()));
    }

    /**
     * Retrieves characters from a specific page of the Rick and Morty API.
     *
     * @param page The page number to retrieve
     * @return A Flux of Character objects from the specified page
     */
    public Flux<Character> getCharactersByPage(int page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CHARACTER_API_PATH)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(CharacterResponse.class)
                .timeout(REQUEST_TIMEOUT)
                .onErrorMap(this::handleApiError)
                .flatMapMany(response -> Flux.fromIterable(response.getResults()));
    }

    /**
     * Retrieves a specific character by its ID from the Rick and Morty API.
     *
     * @param id The ID of the character to retrieve
     * @return A Mono containing the Character if found
     * @throws ResourceNotFoundException if the character with the given ID is not found
     */
    public Mono<Character> getCharacterById(int id) {
        return webClient.get()
                .uri(CHARACTER_API_PATH + "/{id}", id)
                .retrieve()
                .bodyToMono(Character.class)
                .timeout(REQUEST_TIMEOUT)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResourceNotFoundException("Character with ID " + id + " not found"));
                    }
                    return Mono.error(ex);
                })
                .onErrorMap(throwable -> {
                    if (!(throwable instanceof ResourceNotFoundException)) {
                        return handleApiError(throwable);
                    }
                    return throwable;
                });
    }

    /**
     * Searches for characters by name in the Rick and Morty API.
     *
     * @param name The name to search for
     * @return A Flux of Character objects that match the search criteria
     * @throws ResourceNotFoundException if no characters are found with the given name
     */
    public Flux<Character> getCharactersByName(String name) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CHARACTER_API_PATH)
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .bodyToMono(CharacterResponse.class)
                .timeout(REQUEST_TIMEOUT)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResourceNotFoundException("No characters found with the name: " + name));
                    }
                    return Mono.error(ex);
                })
                .onErrorMap(throwable -> {
                    if (!(throwable instanceof ResourceNotFoundException)) {
                        return handleApiError(throwable);
                    }
                    return throwable;
                })
                .flatMapMany(response -> Flux.fromIterable(response.getResults()));
    }

    /**
     * Handles API errors and converts them to appropriate exceptions.
     *
     * @param ex The throwable that occurred during the API request
     * @return A transformed throwable with more specific error information
     */
    private Throwable handleApiError(Throwable ex) {
        if (ex instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) ex;
            log.error("Error in external API: {}. Response: {}", wcre.getStatusCode(), wcre.getResponseBodyAsString());

            if (wcre.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResourceNotFoundException("Resource not found in Rick and Morty API");
            }
        }

        log.error("Error consuming Rick and Morty API", ex);
        return new RuntimeException("Error processing request to external API: " + ex.getMessage());
    }
}