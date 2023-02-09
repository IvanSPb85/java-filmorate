package model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder
public class Film {
    @NonNull
    int id;
    @NonNull
    String name;
    @NonNull
    String description;
    @NonNull
    LocalDate releaseDate;
    @NonNull
    Duration duration;
}
