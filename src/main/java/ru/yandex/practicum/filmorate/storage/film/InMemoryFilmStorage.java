package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validator.FilmValidation;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidation validation;
    private Long counterId;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        validation.isValid(film);
        getNextId();
        film.setId(counterId);
        films.put(counterId, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else throw new NoSuchElementException("Фильм по запросу не найден в хранилище!");
        return film;
    }

    @Override
    public Film getFilm(Long filmId) throws NoSuchElementException {
        return findAllFilms().stream().filter(u -> u.getId().equals(filmId)).findFirst().get();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        getFilm(filmId).getRating().add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) throws NoSuchElementException {
        Film film = getFilm(filmId);
        if (film.getRating().contains(userId)) film.getRating().remove(userId);
        else throw new NoSuchElementException("userId: " + userId + " не найден.");
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        return findAllFilms().stream().sorted((o1, o2) -> o2.getRating().size() - o1.getRating().size())
                .limit(count).collect(Collectors.toList());
    }

    private void getNextId() {
        counterId++;
    }
}
