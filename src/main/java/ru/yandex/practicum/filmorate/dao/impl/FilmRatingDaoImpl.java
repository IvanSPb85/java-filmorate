package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRatingDao;

@Repository
public class FilmRatingDaoImpl implements FilmRatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long filmId, Long userID) {
        String sql = "INSERT INTO film_rating(film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, filmId, userID);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sql = "DELETE FROM film_rating WHERE (film_id = ? AND user_id = ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
