package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Collection<User>> findAllUsers() {
        log.info("Получен запрос к эндпойнту: 'GET /users'");
        return new ResponseEntity<>(service.findAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Получен запрос к эндпойнту: 'POST /users'");
        return new ResponseEntity<>(service.createUser(user), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Получен запрос к эндпойнту: 'PUT /users'");
        return new ResponseEntity<>(service.updateUser(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("Получен запрос к эндпойнту: 'GET /users/{id}'");
        return new ResponseEntity<>(service.findUser(id), HttpStatus.OK);
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
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
        log.info("Получен запрос к эндпойнту: 'GET /users/{id}/friends'");
        return new ResponseEntity<>(service.findFriends(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен запрос к эндпойнту: 'GET /users/{id}/friends/common/{otherId}'");
        return new ResponseEntity<>(service.findCommonFriends(id, otherId), HttpStatus.OK);
    }
}
