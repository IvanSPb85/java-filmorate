package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Category {
    private int id;
    private String name;
}