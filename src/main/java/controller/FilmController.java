package controller;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private int counterId;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection findAllFilms() {
        return films.values();
    }

    @PostMapping("/film")
    public Film createFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпойнту: 'POST /film'");
        try {
            isValid(film);
        } catch (ValidationException e) {
            log.warn("Фильм не прошёл валидацию!", e);
            return film;
        }
        getNextId();
        film.setId(counterId);
        films.put(counterId, film);
        return film;
    }

    @PutMapping("/film")
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпойнту: 'PUT /film'");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else createFilm(film);
        return film;
    }

    private void getNextId() {
        counterId++;
    }

    private void isValid(Film film) throws ValidationException {
        if (film.getName().isBlank())
            throw new ValidationException("название не может быть пустым.");
        if (film.getDescription().length() > 200)
            throw new ValidationException("максимальная длина описания — 200 символов");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        if (film.getDuration().toNanos() <= 0)
            throw new ValidationException("продолжительность фильма должна быть положительной");
    }
}




