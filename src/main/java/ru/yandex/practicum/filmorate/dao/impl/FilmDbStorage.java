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
    private final static String FIND_ALL_FILMS = "SELECT * FROM films";
    private final static String CREATE_FILM = "INSERT INTO films(name, description, release_date, duration, mpa_id)" +
            " VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE_FILM = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa_id = ? WHERE film_id = ?";
    private final static String GET_FILM = "SELECT * FROM films WHERE film_id = ?";
    private final static String FIND_POPULAR_FILM = "SELECT * FROM films AS f " +
            "LEFT OUTER JOIN film_rating AS fr ON f.film_id = fr.film_id " +
            "GROUP BY f.film_id ORDER BY COUNT(fr.user_id) DESC LIMIT ?";
    private final static String EXISTS_FILM = "SELECT COUNT(*) FROM films WHERE film_id = ?";
    private final static String EXISTS_GENRE = "SELECT COUNT(*) FROM film_genre WHERE (genre_id = ? AND film_id = ?)";
    private final static String EXISTS_USER = "SELECT COUNT(*) FROM users WHERE user_id = ?";

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
        return jdbcTemplate.query(FIND_ALL_FILMS, this::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        validation.isValid(film);

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

        if (!(film.getGenres() == null)) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.addGenreToFilm(genre.getId(), filmId);
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        existsFilm(film.getId());
        jdbcTemplate.update(UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
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
        existsFilm(filmId);
        return jdbcTemplate.queryForObject(GET_FILM, this::mapRowToFilm, filmId);
    }

    @Override
    public void addLike(Long id, Long userId) {
        existsUser(userId);
        filmRatingDao.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        existsUser(userId);
        existsFilm(id);
        filmRatingDao.deleteLike(id, userId);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        return jdbcTemplate.query(FIND_POPULAR_FILM, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rawNum) throws SQLException {
        Long filmId = resultSet.getLong("film_id");
        return Film.builder()
                .id(filmId)
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDao.findMpaById(resultSet.getInt("mpa_id")))
                .genres(genreDao.findGenresByFilm(filmId))
                .rating(filmRatingDao.findRatingFilm(filmId))
                .build();
    }

    private boolean existsFilm(long filmId) {
        int result = jdbcTemplate.queryForObject(EXISTS_FILM, Integer.class, filmId);
        if (result != 1) {
            throw new NoSuchElementException("В базе не обнаружен фильм с film_id = " + filmId);
        }
        return true;
    }

    private boolean existsGenre(Integer genreId, Long filmId) {
        return jdbcTemplate.queryForObject(EXISTS_GENRE, Integer.class, genreId, filmId) == 1;
    }

    private boolean existsUser(long userID) {
        int result = jdbcTemplate.queryForObject(EXISTS_USER, Integer.class, userID);
        if (result != 1) {
            throw new NoSuchElementException("В базе не найден пользователь с Id = " + userID);
        }
        return true;
    }
}
