package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final static String FIND_MPA_BY_ID = "SELECT * FROM mpa WHERE mpa_id = ?";
    private final static String FIND_ALL_MPA = "SELECT * FROM mpa";

    private final static String EXISTS_MPA = "SELECT COUNT(*) FROM mpa WHERE mpa_id = ?";

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findMpaById(int mpaId) {
        existsMpa(mpaId);
        return jdbcTemplate.queryForObject(FIND_MPA_BY_ID, this::mapRowToMpa, mpaId);
    }

    @Override
    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query(FIND_ALL_MPA, this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rawNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private boolean existsMpa(int mpaId) {
        int result = jdbcTemplate.queryForObject(EXISTS_MPA, Integer.class, mpaId);
        if (result != 1) {
            throw new NoSuchElementException("mpaId: " + mpaId + " не найден.");
        }
        return true;
    }
}
