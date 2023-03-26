package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findMpaById(int mpaId) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, mpaId);
        if (!sqlRowSet.next()) {
            throw new NoSuchElementException("mpaId: " + mpaId + " не найден.");
        }
        return Mpa.builder()
                .id(sqlRowSet.getInt("mpa_id"))
                .name(sqlRowSet.getString("name"))
                .build();
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rawNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}