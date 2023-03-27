package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {
    private final FilmDbStorage filmStorage;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;

    @BeforeAll
    public void createFilms() {
        Film firstFilm = filmStorage.createFilm(Film.builder()
                .name("firstFilm")
                .description("firstDescription")
                .releaseDate(LocalDate.of(2001, 01, 01))
                .duration(100)
                .mpa(mpaDao.findMpaById(1))
                .genres(List.of(genreDao.findGenreById(1), genreDao.findGenreById(2)))
                .build());

        Film secondFilm = filmStorage.createFilm(Film.builder()
                .name("secondFilm")
                .description("secondDescription")
                .releaseDate(LocalDate.of(2002, 02, 02))
                .duration(200)
                .mpa(mpaDao.findMpaById(2))
                .genres(List.of(genreDao.findGenreById(3), genreDao.findGenreById(4)))
                .build());
    }

    @Test
    public void createFilmTest() {
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilm(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    public void findAllFilmsTest() {
        Optional<Collection<Film>> optionalFilms = Optional.of(filmStorage.findAllFilms());

        assertThat(optionalFilms).isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films).hasSize(2));
    }

    @Test
    public void updateFilmTest() {
        Film updateFilm = filmStorage.createFilm(Film.builder()
                .id(1L)
                .name("updateFilm")
                .description("updateDescription")
                .releaseDate(LocalDate.of(2001, 01, 01))
                .duration(200)
                .mpa(mpaDao.findMpaById(1))
                .genres(List.of(genreDao.findGenreById(1), genreDao.findGenreById(2)))
                .build());
        Optional<Film> filmOptional = Optional.of(filmStorage.updateFilm(updateFilm));

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "updateFilm")
                                .hasFieldOrPropertyWithValue("description", "updateDescription")
                                .hasFieldOrPropertyWithValue("duration", 200));
    }

    @Test
    public void getFilmTest() {
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilm(2L));

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L));
    }

    @Test
    public void addLikeTest() {

    }
}
