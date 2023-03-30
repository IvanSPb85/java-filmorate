package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.servlet.http.HttpServletRequest;

import static ru.yandex.practicum.filmorate.constant.Constant.*;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public ResponseEntity<Collection<Film>> findAllFilms(HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findAllFilms(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film, HttpServletRequest request) {
        log.info(REQUEST_POST_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.createFilm(film), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film, HttpServletRequest request) {
        log.info(REQUEST_PUT_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.updateFilm(film), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findFilm(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        log.info(REQUEST_PUT_LOG, request.getRequestURI());
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId, HttpServletRequest request) {
        log.info(REQUEST_DELETE_LOG, request.getRequestURI());
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> findPopularFilms(@RequestParam(defaultValue = "10") Integer count,
                                                             HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findPopularFilms(count), HttpStatus.OK);
    }
}




