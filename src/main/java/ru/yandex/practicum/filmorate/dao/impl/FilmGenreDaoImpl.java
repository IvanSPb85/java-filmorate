package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;

@Component
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenreToFilm(int genreId, Long filmId) {
        String sql = "INSERT INTO film_genre(genre_id, film_id VALUES(?, ?)";
        jdbcTemplate.update(sql, genreId, filmId);
    }

    @Override
    public void deleteGenreByFilm(int genreId, Long filmId) {
        String sql = "DELETE FROM film_genre WHERE genre_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, genreId, filmId);
    }
}
