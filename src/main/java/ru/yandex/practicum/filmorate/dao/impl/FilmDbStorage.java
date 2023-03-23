package ru.yandex.practicum.filmorate.dao.impl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

public class FilmDbStorage implements FilmStorage {

    @Override
    public Collection<Film> findAllFilms() {
        return null;
    }

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film getFilm(Long id) {
        return null;
    }

    @Override
    public void addLike(Long id, Long userId) {

    }

    @Override
    public void deleteLike(Long id, Long userId) {

    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        return null;
    }
}
