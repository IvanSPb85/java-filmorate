package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(int genreId) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, genreId);
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
