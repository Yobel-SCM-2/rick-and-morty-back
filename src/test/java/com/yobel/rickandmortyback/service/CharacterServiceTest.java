package com.yobel.rickandmortyback.service;

import com.yobel.rickandmortyback.exception.ResourceNotFoundException;
import com.yobel.rickandmortyback.model.Character;
import com.yobel.rickandmortyback.model.response.CharacterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private CharacterService characterService;

    @BeforeEach
    public void setup() {
        characterService = new CharacterService(webClient);
    }

    @Test
    public void getCharacterById_Success() {
        // Arrange
        int characterId = 1;
        Character mockCharacter = new Character();
        mockCharacter.setId(characterId);
        mockCharacter.setName("Rick Sanchez");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), eq(characterId))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Character.class)).thenReturn(Mono.just(mockCharacter));

        // Act & Assert
        StepVerifier.create(characterService.getCharacterById(characterId))
                .expectNextMatches(character -> character.getId().equals(characterId) && character.getName().equals("Rick Sanchez"))
                .verifyComplete();
    }

    @Test
    public void getCharacterById_NotFound() {
        // Arrange
        int characterId = 999;
        WebClientResponseException notFoundException = WebClientResponseException.create(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                null, null, null
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), eq(characterId))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Character.class)).thenReturn(Mono.error(notFoundException));

        // Act & Assert
        StepVerifier.create(characterService.getCharacterById(characterId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    public void getAllCharacters_Success() {
        // Arrange
        List<Character> characters = new ArrayList<>();
        Character character1 = new Character();
        character1.setId(1);
        character1.setName("Rick Sanchez");
        characters.add(character1);

        Character character2 = new Character();
        character2.setId(2);
        character2.setName("Morty Smith");
        characters.add(character2);

        CharacterResponse response = new CharacterResponse();
        response.setResults(characters);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CharacterResponse.class)).thenReturn(Mono.just(response));

        // Act & Assert
        StepVerifier.create(characterService.getAllCharacters())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void getAllCharacters_Error() {
        // Arrange
        RuntimeException mockException = new RuntimeException("API Error");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CharacterResponse.class)).thenReturn(Mono.error(mockException));

        // Act & Assert
        StepVerifier.create(characterService.getAllCharacters())
                .expectError(RuntimeException.class)
                .verify();
    }
}
