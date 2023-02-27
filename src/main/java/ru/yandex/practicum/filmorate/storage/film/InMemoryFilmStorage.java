package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validator.FilmValidation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidation validation;
    private int counterId;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        try {
            validation.isValid(film);
        } catch (ValidationException e) {
            log.warn("Фильм не прошёл валидацию по причине: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        }
        getNextId();
        film.setId(counterId);
        films.put(counterId, film);
        return ResponseEntity.ok(film);
    }

    @Override
    public ResponseEntity<Film> updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм по запросу не найден в хранилище!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
        return ResponseEntity.ok(film);
    }

    private void getNextId() {
        counterId++;
    }
}
