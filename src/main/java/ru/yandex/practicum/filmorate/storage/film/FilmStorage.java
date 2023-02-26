package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAllFilms();

    ResponseEntity<Film> createFilm(Film film);

    ResponseEntity<Film> updateFilm(Film film);

}
