package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTests {
    private final GenreDao genreDao;

    @Test
    public void findGenreById() {
        Optional<Genre> genreOptional = Optional.of(genreDao.findGenreById(3));

        assertThat(genreOptional).isPresent().hasValueSatisfying(
                genre -> assertThat(genre).hasFieldOrPropertyWithValue("name", "Мультфильм")
        );
    }

    @Test
    public void findAllGenreTest() {
        Optional<List<Genre>> optionalGenres = Optional.of(genreDao.findAllGenres());

        assertThat(optionalGenres).isPresent().hasValueSatisfying(
                genres -> assertThat(genres).hasSize(6)
        );
    }
}
