package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private int counterId;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        log.info("Получен запрос к эндпойнту: 'POST /users'");
        try {
            isValid(user);
        } catch (ValidationException e) {
            log.warn("Пользователь не прошёл валидацию по причине: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
        getNextId();
        user.setId(counterId);
        users.put(counterId, user);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        log.info("Получен запрос к эндпойнту: 'PUT /users'");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.warn("Данный пользователь не найден!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.ok(user);
    }

    private void getNextId() {
        counterId++;
    }

    public void isValid(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@"))
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().split(" ").length > 1)
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("дата рождения не может быть в будущем");
    }
}
