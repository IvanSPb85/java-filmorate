package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTests {
    private final MpaDao mpaDao;

    @Test
    public void findMpaByIdTest() {
        Optional<Mpa> mpaOptional = Optional.of(mpaDao.findMpaById(3));

        assertThat(mpaOptional).isPresent().hasValueSatisfying(
                mpa -> assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13")
        );
    }

    @Test
    public void findAllMpaTest() {
        Optional<List<Mpa>> mpaOptional = Optional.of(mpaDao.findAllMpa());

        assertThat(mpaOptional).isPresent().hasValueSatisfying(
                mpa -> assertThat(mpa).hasSize(5)
        );
    }
}
