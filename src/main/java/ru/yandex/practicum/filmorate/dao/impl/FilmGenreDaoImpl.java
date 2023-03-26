package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;

@Repository
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final static String ADD_GENRE_TO_FILM = "INSERT INTO film_genre(genre_id, film_id) VALUES(?, ?)";
    private final static String DELETE_GENRE_BY_FILM = "DELETE FROM film_genre WHERE film_id = ?";

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenreToFilm(int genreId, Long filmId) {
        jdbcTemplate.update(ADD_GENRE_TO_FILM, genreId, filmId);
    }

    @Override
    public void deleteGenreByFilm(Long filmId) {
        jdbcTemplate.update(DELETE_GENRE_BY_FILM, filmId);
    }
}
