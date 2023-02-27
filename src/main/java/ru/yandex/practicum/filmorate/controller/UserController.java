package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Получен запрос к эндпойнту: 'GET /users'");
        return service.storage.findAllUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Получен запрос к эндпойнту: 'POST /users'");
        return service.storage.createUser(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Получен запрос к эндпойнту: 'PUT /users'");
        return service.storage.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Получен запрос к эндпойнту: 'GET /users/{id}'");
        return service.findUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос к эндпойнту: 'PUT /users/{id}/friends/{friendId}'");
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен запрос к эндпойнту: 'DELETE /users/{id}/friends/{friendId}'");
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Получен запрос к эндпойнту: 'GET /users/{id}/friends'");
        return service.findFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен запрос к эндпойнту: 'GET /users/{id}/friends/common/{otherId}'");
        return service.findCommonFriends(id, otherId);
    }
}
