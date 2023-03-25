package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FilmRatingDao {
    Set<Long> findRatingByFilm(Long filmId);

    void addLike(Long filmId, Long userID);

    void deleteLike(Long filmId, Long userId);
}
