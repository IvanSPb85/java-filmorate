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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public ResponseEntity<Collection<Film>> findAllFilms() {
        log.info("Получен запрос к эндпойнту: 'GET /films'");
        return new ResponseEntity<>(service.findAllFilms(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпойнту: 'POST /films'");
        return new ResponseEntity<>(service.createFilm(film), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        log.info("Получен запрос к эндпойнту: 'PUT /films'");
        return new ResponseEntity<>(service.updateFilm(film), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable("id") Long id) {
        log.info("Получен запрос к эндпойнту: 'GET /films/{id}'");
        return new ResponseEntity<>(service.findFilm(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту: 'PUT /films/{id}/like/{userId}'");
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос к эндпойнту: 'DELETE /films/{id}/like/{userId}'");
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> findPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен запрос к эндпойнту: 'GET /films/popular?count={count}'");
        return new ResponseEntity<>(service.findPopularFilms(count), HttpStatus.OK);
    }
}




