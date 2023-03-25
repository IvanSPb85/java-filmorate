package ru.yandex.practicum.filmorate.dao;

public interface FilmGenreDao {
    void addGenreToFilm(int genreId, Long filmId);

    void deleteGenreByFilm(int genreId, Long filmId);
}
