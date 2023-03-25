package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;

@Repository
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenreToFilm(int genreId, Long filmId) {
        String sql = "INSERT INTO film_genre(genre_id, film_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, genreId, filmId);
    }

    @Override
    public void deleteGenreByFilm(Long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
