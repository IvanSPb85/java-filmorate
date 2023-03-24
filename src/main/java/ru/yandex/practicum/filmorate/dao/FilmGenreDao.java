package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmGenreDao {
    Set<Genre> findGenresByFilm(Long filmId);
}
