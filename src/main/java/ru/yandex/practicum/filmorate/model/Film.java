package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private final Set<Long> rating;
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
