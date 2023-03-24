package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static ru.yandex.practicum.filmorate.constant.Constant.REQUEST_GET_LOG;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping
    public ResponseEntity<List<Genre>> findAllUsers(HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findAllGenres(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getUser(@PathVariable Integer id, HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findGenreById(id), HttpStatus.OK);
    }
}
