package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    Mpa findMpaById(int mpaId);

    List<Mpa> findAllMpa();
}
