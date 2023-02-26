package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAllUsers();
    ResponseEntity<User> createUser(User user);
    ResponseEntity<User> updateUser(User user);
}
