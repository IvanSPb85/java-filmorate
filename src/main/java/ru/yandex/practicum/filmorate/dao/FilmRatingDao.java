package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FilmRatingDao {

    void addLike(Long filmId, Long userID);

    void deleteLike(Long filmId, Long userId);

    Set<Long> findRatingFilm(Long filmId);
}
