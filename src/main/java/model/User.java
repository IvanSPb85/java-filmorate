package model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    @NonNull
    private final int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
