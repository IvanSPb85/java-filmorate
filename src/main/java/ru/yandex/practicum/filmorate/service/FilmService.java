package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(Long filmId, Long userId) {
        storage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) throws NoSuchElementException {
        storage.deleteLike(filmId, userId);
    }

    public List<Film> findPopularFilms(Integer count) {
        return storage.findPopularFilms(count);
    }

    public Film findFilm(Long filmId) {
        return storage.getFilm(filmId);
    }

    public Collection<Film> findAllFilms() {
        return storage.findAllFilms();
    }

    public Film createFilm(Film film) {
        return storage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }
}


