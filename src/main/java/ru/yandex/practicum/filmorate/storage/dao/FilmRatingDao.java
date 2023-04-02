package ru.yandex.practicum.filmorate.storage.dao;

public interface FilmRatingDao {

    void addLike(Long filmId, Long userID);

    void deleteLike(Long filmId, Long userId);
}
