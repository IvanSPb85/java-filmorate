package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class Film {
    @Builder.Default
    private final Set<Long> rating = new TreeSet<>();
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    @Builder.Default
    private final List<Genre> genres = new ArrayList<>();
}
