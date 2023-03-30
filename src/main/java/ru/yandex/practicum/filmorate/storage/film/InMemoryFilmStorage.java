package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private Long counterId = 0L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
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
        Film film = getFilm(filmId);
        film.setRating(film.getRating() + 1);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) throws NoSuchElementException {
        Film film = getFilm(filmId);
        film.setRating(film.getRating() - 1);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        return findAllFilms().stream().sorted((o1, o2) -> o2.getRating() - o1.getRating())
                .limit(count).collect(Collectors.toList());
    }

    private void getNextId() {
        counterId++;
    }
}
