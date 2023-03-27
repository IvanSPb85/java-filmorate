package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class User {
    @Builder.Default
    private final Set<Long> friends = new TreeSet<>();
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private final Map<Long, Boolean> friendStatus = new HashMap<>();
}
