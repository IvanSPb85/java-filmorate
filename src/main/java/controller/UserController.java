package controller;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private int counterId;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Collection findAllUsers() {
        return users.values();
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос к эндпойнту: 'POST /user'");
        try {
            isValid(user);
        } catch (ValidationException e) {
            log.warn("Пользователь не прошёл валидацию!", e);
            return user;
        }
        getNextId();
        user.setId(counterId);
        users.put(counterId, user);
        return user;
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос к эндпойнту: 'PUT /user'");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else return createUser(user);
    }

    private void getNextId() {
        counterId++;
    }

    private void isValid(User user) throws ValidationException {
        if (user.getEmail().isBlank() | !user.getEmail().contains("@"))
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        if (user.getLogin().isBlank() | user.getLogin().contains(" "))
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        if (user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("дата рождения не может быть в будущем");
    }
}
