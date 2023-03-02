package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@Component
public class FilmValidation {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, Month.DECEMBER, 28);

    public void isValid(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank())
            throw new ValidationException("название не может быть пустым.");
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH)
            throw new ValidationException("максимальная длина описания — 200 символов");
        if (film.getReleaseDate().isBefore(BIRTHDAY_MOVIE))
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        if (film.getDuration() <= 0)
            throw new ValidationException("продолжительность фильма должна быть положительной");
    }
}
