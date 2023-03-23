package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class User {
    private final Set<Long> friends = new TreeSet<>();
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Map<Integer, Boolean> friendStatus;

}
