package com.yobel.rickandmortyback.model;

import java.time.LocalDateTime;
import java.util.List;

public class Episode {
    private Integer id;
    private String name;
    private String airDate;
    private String episode;
    private List<String> characters;
    private String url;
    private LocalDateTime created;
}
