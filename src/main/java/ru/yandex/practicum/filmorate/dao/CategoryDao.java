package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Category;

import java.util.List;

public interface CategoryDao {
    Category findCategoryById(int categoryId);

    List<Category> findAllCategory();
}
