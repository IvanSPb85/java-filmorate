package ru.yandex.practicum.filmorate.storage.dao;

public interface FilmGenreDao {
    void addGenreToFilm(int genreId, Long filmId);

    void deleteGenreByFilm(Long filmId);
}
