package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int counterId;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film, HttpServletResponse response) {
        log.info("Получен запрос к эндпойнту: 'POST /films'");
        try {
            isValid(film);
        } catch (ValidationException e) {
            log.warn("Фильм не прошёл валидацию!", e);
            response.setStatus(400);
            return film;
        }
        getNextId();
        film.setId(counterId);
        films.put(counterId, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film, HttpServletResponse response) {
        log.info("Получен запрос к эндпойнту: 'PUT /films'");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм по запросу не найден в хранилище!");
            response.setStatus(404);
        }
        return film;
    }

    private void getNextId() {
        counterId++;
    }

    public void isValid(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank())
            throw new ValidationException("название не может быть пустым.");
        if (film.getDescription().length() > 200)
            throw new ValidationException("максимальная длина описания — 200 символов");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)))
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        if (film.getDuration() <= 0)
            throw new ValidationException("продолжительность фильма должна быть положительной");
    }
}




