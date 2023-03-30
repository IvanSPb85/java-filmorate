package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmExtractor filmExtractor;
    private static final String FIND_ALL_FILMS =
            "SELECT f.film_id, f.name, f.description, f.release_date, f.duration," +
                    " f.mpa_id, m.name, fg.genre_id, g.name," +
                    "COUNT(fr.user_id) FROM films AS f " +
                    "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                    "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                    "LEFT JOIN film_rating AS fr ON f.film_id = fr.film_id " +
                    "GROUP BY f.film_id, fg.genre_id";
    private static final String CREATE_FILM = "INSERT INTO films(name, description, release_date, duration, mpa_id)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa_id = ? WHERE film_id = ?";
    private static final String GET_FILM = "SELECT f.film_id, f.name, f.description," +
            " f.release_date, f.duration, f.mpa_id, m.name, fg.genre_id, g.name," +
            "COUNT(fr.user_id) FROM films as f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN film_rating AS fr ON f.film_id = fr.film_id " +
            "WHERE f.film_id = ? " +
            "GROUP BY fg.genre_id";
    private static final String FIND_POPULAR_FILM = "SELECT f.film_id, f.name, f.description," +
            " f.release_date, f.duration, f.mpa_id, m.name, fg.genre_id, g.name," +
            "COUNT(fr.user_id) FROM films as f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN film_rating AS fr ON f.film_id = fr.film_id " +
            "GROUP BY f.film_id, fg.genre_id " +
            "ORDER BY COUNT(fr.user_id) DESC LIMIT ?";
    private static final String EXISTS_FILM = "SELECT COUNT(*) FROM films WHERE film_id = ?";
    private static final String EXISTS_USER = "SELECT COUNT(*) FROM users WHERE user_id = ?";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmExtractor filmExtractor) {
        this.filmExtractor = filmExtractor;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> findAllFilms() {
        Map<Film, List<Genre>> filmListMap = jdbcTemplate.query(FIND_ALL_FILMS, filmExtractor);
        filmListMap.forEach(Film::setGenres);
        return filmListMap.keySet();
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_FILM, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        existsFilm(film.getId());
        jdbcTemplate.update(UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public Film getFilm(Long filmId) {
        existsFilm(filmId);
        Map<Film, List<Genre>> filmListMap = jdbcTemplate.query(GET_FILM, filmExtractor, filmId);
        filmListMap.forEach(Film::setGenres);
        return filmListMap.keySet().stream().findFirst().get();
    }

    @Override
    public void addLike(Long id, Long userId) {
        existsUser(userId);
        existsFilm(id);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        existsUser(userId);
        existsFilm(id);
    }

    @Override
    public Collection<Film> findPopularFilms(Integer count) {
        Map<Film, List<Genre>> filmListMap = jdbcTemplate.query(FIND_POPULAR_FILM, filmExtractor, count);
        filmListMap.forEach(Film::setGenres);
        return filmListMap.keySet();
    }

    private boolean existsFilm(long filmId) {
        int result = jdbcTemplate.queryForObject(EXISTS_FILM, Integer.class, filmId);
        if (result != 1) {
            throw new NoSuchElementException("В базе не обнаружен фильм с film_id = " + filmId);
        }
        return true;
    }

    private boolean existsUser(long userID) {
        int result = jdbcTemplate.queryForObject(EXISTS_USER, Integer.class, userID);
        if (result != 1) {
            throw new NoSuchElementException("В базе не найден пользователь с Id = " + userID);
        }
        return true;
    }
}
