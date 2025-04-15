package com.yobel.rickandmortyback.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Configuration class for setting up a WebClient instance to interact with the Rick and Morty API.
 * <p>
 * This class configures a WebClient bean with appropriate settings for making HTTP requests
 * to the Rick and Morty API. It includes configurations for:
 * <ul>
 *     <li>Base URL for the API</li>
 *     <li>Default headers</li>
 *     <li>Memory allocation for responses</li>
 *     <li>Request and response logging</li>
 * </ul>
 * </p>
 */
@Configuration
@Log4j2
public class WebClientConfig {
    /**
     * Base URL for the Rick and Morty API
     */
    private static final String BASE_URL = "https://rickandmortyapi.com/api";

    /**
     * Creates and configures a WebClient bean for making HTTP requests to the Rick and Morty API.
     *
     * @return A configured WebClient instance ready for making API requests
     */
    @Bean
    public WebClient webClient() {
        final int size = 16 * 1024 * 1024;

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(strategies)
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    /**
     * Creates a filter function that logs details about outgoing HTTP requests.
     *
     * @return An ExchangeFilterFunction that logs request information
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Making {} request to {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) ->
                    values.forEach(value -> log.debug("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    /**
     * Creates a filter function that logs details about incoming HTTP responses.
     *
     * @return An ExchangeFilterFunction that logs response information
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response received with status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}