package com.yobel.rickandmortyback.model.response;

import com.yobel.rickandmortyback.model.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterResponse {
    private Info info;
    private List<Character> results;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        private Integer count;
        private Integer pages;
        private String next;
        private String prev;
    }
}
