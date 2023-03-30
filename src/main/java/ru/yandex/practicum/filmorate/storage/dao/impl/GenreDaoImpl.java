package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String FIND_ALL_GENRES = "SELECT * FROM genre";
    private static final String FIND_GENRE_BY_FILM = "SELECT * FROM genre WHERE genre_id IN (" +
            "SELECT genre_id FROM film_genre WHERE film_id = ?)";

    private static final String EXISTS_GENRE = "SELECT COUNT(*) FROM genre WHERE genre_id = ?";

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(int genreId) {
        existsGenre(genreId);
        return jdbcTemplate.queryForObject(FIND_GENRE_BY_ID, this::mapRowToGenre, genreId);
    }

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query(FIND_ALL_GENRES, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rawNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private boolean existsGenre(int genreId) {
        int result = jdbcTemplate.queryForObject(EXISTS_GENRE, Integer.class, genreId);
        if (result != 1) {
            throw new NoSuchElementException("genreId: " + genreId + " не найден.");
        }
        return true;
    }
}
