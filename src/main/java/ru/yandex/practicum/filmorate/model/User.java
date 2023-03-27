package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {
    private final Set<Long> friends;
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Map<Long, Boolean> friendStatus;
}
