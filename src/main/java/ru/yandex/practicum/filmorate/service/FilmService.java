package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.storage.dao.FilmRatingDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidation;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage storage;
    private final FilmGenreDao filmGenreDao;
    private final FilmRatingDao filmRatingDao;
    private final FilmValidation validation;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage, FilmGenreDao filmGenreDao,
                       FilmRatingDao filmRatingDao, FilmValidation validation) {
        this.storage = storage;
        this.filmGenreDao = filmGenreDao;
        this.filmRatingDao = filmRatingDao;
        this.validation = validation;
    }

    public void addLike(Long filmId, Long userId) {
        storage.addLike(filmId, userId);
        filmRatingDao.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) throws NoSuchElementException {
        storage.deleteLike(filmId, userId);
        filmRatingDao.deleteLike(filmId, userId);
    }

    public Collection<Film> findPopularFilms(Integer count) {
        return storage.findPopularFilms(count);
    }

    public Film findFilm(Long filmId) {
        return storage.getFilm(filmId);
    }

    public Collection<Film> findAllFilms() {
        return storage.findAllFilms();
    }

    public Film createFilm(Film film) {
        validation.isValid(film);
        film = storage.createFilm(film);
        if (!(film.getGenres() == null)) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.addGenreToFilm(genre.getId(), film.getId());
            }
        }
        return film;
    }

    public Film updateFilm(Film film) {
        storage.updateFilm(film);
        filmGenreDao.deleteGenreByFilm(film.getId());
        if (film.getGenres() == null) return film;
        List<Genre> distinctGenres = film.getGenres().stream().distinct().collect(Collectors.toList());
        for (Genre genre : distinctGenres) {
            filmGenreDao.addGenreToFilm(genre.getId(), film.getId());
        }
        film.setGenres(distinctGenres);
        return film;
    }
}


