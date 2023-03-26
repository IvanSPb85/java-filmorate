package ru.yandex.practicum.filmorate.dao;

public interface FilmRatingDao {

    void addLike(Long filmId, Long userID);

    void deleteLike(Long filmId, Long userId);
}
