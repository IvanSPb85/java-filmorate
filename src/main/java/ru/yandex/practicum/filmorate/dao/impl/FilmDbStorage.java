package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.FilmRatingDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreDao filmGenreDao;
    private final FilmRatingDao filmRatingDao;
    private final MpaDao mpaDao;
    private final FilmValidation validation;
    private final GenreDao genreDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenreDao filmGenreDao,
                         FilmRatingDao filmRatingDao, MpaDao mpaDao, FilmValidation validation, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDao = filmGenreDao;
        this.filmRatingDao = filmRatingDao;
        this.mpaDao = mpaDao;
        this.validation = validation;
        this.genreDao = genreDao;
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        validation.isValid(film);
        String sql = "INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        if (!(film.getGenres() == null)) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.addGenreToFilm(genre.getId(), filmId);
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!existsFilm(film.getId())) {
            throw new NoSuchElementException("В базе не найден фильм с Id = " + film.getId());
        }
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        filmGenreDao.deleteGenreByFilm(film.getId());

        if (film.getGenres() == null) return film;

        for (Genre genre : film.getGenres()) {
            if (!existsGenre(genre.getId(), film.getId())) {
                filmGenreDao.addGenreToFilm(genre.getId(), film.getId());
            }
        }
        return getFilm(film.getId());
    }

    @Override
    public Film getFilm(Long filmId) {
        if (!existsFilm(filmId)) {
            throw new NoSuchElementException("В базе не обнаружен филь с film_id = " + filmId);
        }
        String sql = "SELECT * FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, filmId);
    }

    @Override
    public void addLike(Long id, Long userId) {
        filmRatingDao.addLike(id, userId);

    }

    @Override
    public void deleteLike(Long id, Long userId) {
        if (!existsFilm(userId)) {
            throw new NoSuchElementException("Пользователь с userId = " + userId + " не найден в базе.");
        }
        filmRatingDao.deleteLike(id, userId);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        String sql = "SELECT * FROM films AS f LEFT OUTER JOIN film_rating AS fr ON f.film_id = fr.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(fr.user_id) DESC LIMIT " + count;
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rawNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDao.findMpaById(resultSet.getInt("mpa_id")))
                .genres(genreDao.findGenresByFilm(resultSet.getLong("film_id")))
                .build();
    }

    private boolean existsFilm(long filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM films WHERE film_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
        return result == 1;
    }

    private boolean existsGenre(Integer genreId, Long filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM film_genre WHERE (genre_id = ? AND film_id = ?)";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, genreId, filmId) == 1;
    }
}
