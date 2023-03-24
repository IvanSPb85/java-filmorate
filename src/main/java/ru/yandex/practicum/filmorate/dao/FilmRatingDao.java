package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FilmRatingDao {
    Set<Long> findRatingByFilm(Long filmId);

    Long addLike(Long filmId, Long userID);

    Long deleteLike(Long filmId, Long userId);
}
