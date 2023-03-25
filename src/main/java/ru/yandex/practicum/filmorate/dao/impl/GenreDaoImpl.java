package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(int genreId) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, genreId);
        if (!sqlRowSet.next()) {
            throw new NoSuchElementException("genreId: " + genreId + " не найден.");
        }
        return Genre.builder()
                .id(sqlRowSet.getInt("genre_id"))
                .name(sqlRowSet.getString("name"))
                .build();
    }

    @Override
    public List<Genre> findAllGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public List<Genre> findGenresByFilm(Long filmId) {
        String sql = "SELECT * FROM genre WHERE genre_id IN (SELECT genre_id FROM film_genre WHERE film_id = " +
                filmId + ")";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rawNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
