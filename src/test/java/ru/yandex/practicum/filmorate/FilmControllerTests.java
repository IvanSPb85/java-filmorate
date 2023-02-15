package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;

public class FilmControllerTests {
    FilmController controller = new FilmController();
    Film film;

    @BeforeEach
    public void createFilm() {
        film = Film.builder()
                .name("name")
                .description("a".repeat(200))
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 28))
                .duration(100)
                .build();
    }

    @Test
    public void isValidFilmNotThrowValidateExceptionTest() {
        controller.isValid(film);
    }

    @Test
    public void isValidFilmNameNullMustThrowValidateExceptionTest() {
        film.setName(null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(film));
        assertEquals("название не может быть пустым.", exception.getMessage());
    }

    @Test
    public void isValidFilmNameEmptyMustThrowValidateExceptionTest() {
        film.setName(" ");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(film));
        assertEquals("название не может быть пустым.", exception.getMessage());
    }

    @Test
    public void isValidFilmFailDescriptionMustThrowValidateExceptionTest() {
        film.setDescription("a".repeat(201));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(film));
        assertEquals("максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    public void isValidFilmFailReleaseDateMustThrowValidateExceptionTest() {
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(film));
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    public void isValidFilmNotPositiveDurationMustThrowValidateExceptionTest() {
        film.setDuration(0);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(film));
        assertEquals("продолжительность фильма должна быть положительной", exception.getMessage());
    }
}
