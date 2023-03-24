package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Category;

import java.util.Collection;

public interface CategoryDao {
    Category findCategoryById();

    Collection<Category> findAllCategory();
}
