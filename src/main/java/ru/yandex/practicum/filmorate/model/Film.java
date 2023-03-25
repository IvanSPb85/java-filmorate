package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class Film {
    private final Set<Long> rating = new TreeSet<>();
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private List<Genre> genres;
}
