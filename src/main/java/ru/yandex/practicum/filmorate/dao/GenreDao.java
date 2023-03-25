package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    Genre findGenreById(int genreId);

    List<Genre> findAllGenres();

    List<Genre> findGenresByFilm(Long filmId);
}
