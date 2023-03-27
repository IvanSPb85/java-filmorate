package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRatingDao;

import java.util.HashSet;
import java.util.Set;

@Repository
public class FilmRatingDaoImpl implements FilmRatingDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String ADD_LIKE = "INSERT INTO film_rating(film_id, user_id) VALUES(?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM film_rating WHERE (film_id = ? AND user_id = ?)";

    private static final String FIND_RATING_FILM = "SELECT user_id FROM film_rating WHERE film_id = ?";

    @Autowired
    public FilmRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long filmId, Long userID) {
        jdbcTemplate.update(ADD_LIKE, filmId, userID);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbcTemplate.update(DELETE_LIKE, filmId, userId);
    }

    @Override
    public Set<Long> findRatingFilm(Long filmId) {
        return new HashSet<>(jdbcTemplate.query(FIND_RATING_FILM, (rs, rowNum) ->
                (rs.getLong("user_id")), filmId));
    }
}
