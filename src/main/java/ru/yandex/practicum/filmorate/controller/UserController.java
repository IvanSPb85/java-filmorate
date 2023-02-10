package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int counterId;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user, HttpServletResponse response) {
        log.info("Получен запрос к эндпойнту: 'POST /users'");
        try {
            isValid(user);
        } catch (ValidationException e) {
            log.warn("Пользователь не прошёл валидацию!", e);
            response.setStatus(400);
            return user;
        }
        getNextId();
        user.setId(counterId);
        users.put(counterId, user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user, HttpServletResponse response) {
        log.info("Получен запрос к эндпойнту: 'PUT /users'");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            response.setStatus(404);
        }
        return user;
    }

    private void getNextId() {
        counterId++;
    }

    private void isValid(User user) throws ValidationException {
        if (user.getEmail().isBlank() | !user.getEmail().contains("@"))
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        if (user.getLogin().isBlank() | user.getLogin().split(" ").length > 1)
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        if (user.getName() == null) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("дата рождения не может быть в будущем");
    }
}
