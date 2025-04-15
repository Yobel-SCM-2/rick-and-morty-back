package com.yobel.rickandmortyback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Integer id;
    private String name;
    private String url;
    private String type;
    private String dimension;
    private List<String> residents;
    private LocalDateTime created;
}
