package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public final FilmStorage storage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(Long filmId, Long userId) {
        findFilm(filmId).getRating().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        findFilm(filmId).getRating().remove(userId);
    }

    public List<Film> findPopularFilms(int count) {
        return storage.findAllFilms().stream().sorted((o1, o2) -> o2.getRating().size() - o1.getRating().size())
                .limit(count).collect(Collectors.toList());
    }

    public Film findFilm(Long filmId) throws NoSuchElementException {
        return storage.findAllFilms().stream().filter(u -> u.getId() == filmId).findFirst().get();
    }
}
