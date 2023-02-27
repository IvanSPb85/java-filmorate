package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final UserValidation validation;
    private int counterId;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        try {
            validation.isValid(user);
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
}
