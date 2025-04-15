package com.yobel.rickandmortyback.controller;

import com.yobel.rickandmortyback.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yobel.rickandmortyback.model.Character;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/characters")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Character> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @GetMapping(path = "/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Character> getCharactersByPage(@PathVariable int page) {
        return characterService.getCharactersByPage(page);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Character>> getCharacterById(@PathVariable int id) {
        return characterService.getCharacterById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Character> searchCharactersByName(@RequestParam String name) {
        return characterService.getCharactersByName(name);
    }
}
