package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

import static ru.yandex.practicum.filmorate.constant.Constant.*;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<Collection<User>> findAllUsers(HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, HttpServletRequest request) {
        log.info(REQUEST_POST_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.createUser(user), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user, HttpServletRequest request) {
        log.info(REQUEST_PUT_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.updateUser(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id, HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findUser(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId,
                          HttpServletRequest request) {
        log.info(REQUEST_PUT_LOG, request.getRequestURI());
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId, HttpServletRequest request) {
        log.info(REQUEST_DELETE_LOG, request.getRequestURI());
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id, HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findFriends(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId,
                                                       HttpServletRequest request) {
        log.info(REQUEST_GET_LOG, request.getRequestURI());
        return new ResponseEntity<>(service.findCommonFriends(id, otherId), HttpStatus.OK);
    }
}
