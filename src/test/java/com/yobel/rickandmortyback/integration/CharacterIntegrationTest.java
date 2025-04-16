package com.yobel.rickandmortyback.integration;

import com.yobel.rickandmortyback.model.Character;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WebClient webClient;

    @Test
    public void testGetCharacterFromApiIntegration() {
        // Este test verifica la conexión real a la API externa (solo un caso como se solicita)
        // Asume que el personaje con ID 1 (Rick Sanchez) siempre existe

        // Primero obtenemos el personaje directamente de la API externa
        Mono<Character> externalApiCharacter = webClient.get()
                .uri("/character/1")
                .retrieve()
                .bodyToMono(Character.class);

        // Ahora obtenemos el mismo personaje a través de nuestro endpoint
        webTestClient.get()
                .uri("/api/characters/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Character.class)
                .value(character -> {
                    assert character.getId() == 1;
                    assert character.getName().equals("Rick Sanchez");
                });
    }
}