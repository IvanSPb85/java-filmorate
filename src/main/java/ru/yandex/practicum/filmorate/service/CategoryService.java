package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.CategoryDao;
import ru.yandex.practicum.filmorate.model.Category;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public Category findCategoryById(int categoryId) {
        return categoryDao.findCategoryById(categoryId);
    }

    public List<Category> findAllCategory() {
        return categoryDao.findAllCategory();
    }
}
