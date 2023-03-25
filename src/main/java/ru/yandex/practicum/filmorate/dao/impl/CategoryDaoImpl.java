package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.CategoryDao;
import ru.yandex.practicum.filmorate.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {
    private final JdbcTemplate jdbcTemplate;

    public CategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Category findCategoryById(int categotyId) {
        String sql = "SELECT * FROM category WHERE category_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToCategory, categotyId);
    }

    @Override
    public List<Category> findAllCategory() {
        String sql = "SELECT * FROM category";
        return jdbcTemplate.query(sql, this::mapRowToCategory);
    }

    private Category mapRowToCategory(ResultSet resultSet, int rawNum) throws SQLException {
        return Category.builder()
                .id(resultSet.getInt("category_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
