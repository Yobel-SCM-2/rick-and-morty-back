package com.yobel.rickandmortyback.controller;

import com.yobel.rickandmortyback.exception.ResourceNotFoundException;
import com.yobel.rickandmortyback.model.Character;
import com.yobel.rickandmortyback.service.CharacterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CharacterControllerTest {

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterController characterController;

    @Test
    public void getCharacterById_Success() {
        // Arrange
        Character mockCharacter = new Character();
        mockCharacter.setId(1);
        mockCharacter.setName("Rick Sanchez");
        when(characterService.getCharacterById(anyInt())).thenReturn(Mono.just(mockCharacter));

        WebTestClient testClient = WebTestClient.bindToController(characterController).build();

        // Act & Assert
        testClient.get()
                .uri("/api/characters/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Character.class)
                .isEqualTo(mockCharacter);
    }

    @Test
    public void getCharacterById_NotFound() {
        // Arrange
        when(characterService.getCharacterById(anyInt()))
                .thenReturn(Mono.error(new ResourceNotFoundException("Character not found")));

        WebTestClient testClient = WebTestClient.bindToController(characterController).build();

        // Act & Assert
        testClient.get()
                .uri("/api/characters/999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void getAllCharacters_Success() {
        // Arrange
        Character character1 = new Character();
        character1.setId(1);
        character1.setName("Rick Sanchez");

        Character character2 = new Character();
        character2.setId(2);
        character2.setName("Morty Smith");

        when(characterService.getAllCharacters()).thenReturn(Flux.just(character1, character2));

        WebTestClient testClient = WebTestClient.bindToController(characterController).build();

        // Act & Assert
        testClient.get()
                .uri("/api/characters")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Character.class)
                .hasSize(2);
    }

    @Test
    public void searchCharactersByName_Success() {
        // Arrange
        Character character = new Character();
        character.setId(1);
        character.setName("Rick Sanchez");

        when(characterService.getCharactersByName(anyString())).thenReturn(Flux.just(character));

        WebTestClient testClient = WebTestClient.bindToController(characterController).build();

        // Act & Assert
        testClient.get()
                .uri("/api/characters/search?name=Rick")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Character.class)
                .hasSize(1);
    }

    @Test
    public void searchCharactersByName_NotFound() {
        // Arrange
        when(characterService.getCharactersByName(anyString()))
                .thenReturn(Flux.error(new ResourceNotFoundException("No characters found with this name")));

        WebTestClient testClient = WebTestClient.bindToController(characterController).build();

        // Act & Assert
        testClient.get()
                .uri("/api/characters/search?name=NonExistentCharacter")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}