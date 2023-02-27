package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> findAllFilms() {
        return service.storage.findAllFilms();
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        return service.storage.createFilm(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        return service.storage.updateFilm(film);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findFilm(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> findPopularFilms(
            @RequestParam(defaultValue = "10") Integer count) {
        return new ResponseEntity<>(service.findPopularFilms(count), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleIllegalCount(final NoSuchElementException e) {
        return new ResponseEntity<>(Map.of("error", "film not found"), HttpStatus.NOT_FOUND);
    }
}




