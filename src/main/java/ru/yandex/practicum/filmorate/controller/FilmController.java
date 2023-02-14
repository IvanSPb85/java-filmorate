package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, Month.DECEMBER, 28);

    private int counterId;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпойнту: 'POST /films'");
        try {
            isValid(film);
        } catch (ValidationException e) {
            log.warn("Фильм не прошёл валидацию по причине: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        }
        getNextId();
        film.setId(counterId);
        films.put(counterId, film);
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпойнту: 'PUT /films'");
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

    public void isValid(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank())
            throw new ValidationException("название не может быть пустым.");
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH)
            throw new ValidationException("максимальная длина описания — 200 символов");
        if (film.getReleaseDate().isBefore(BIRTHDAY_MOVIE))
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        if (film.getDuration() <= 0)
            throw new ValidationException("продолжительность фильма должна быть положительной");
    }
}




