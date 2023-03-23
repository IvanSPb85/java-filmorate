package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long userId, Long friendId) {
        storage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        storage.deleteFriend(userId, friendId);
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        return storage.getCommonFriends(userId, otherId);
    }

    public List<User> findFriends(Long userId) {
        return storage.getFriends(userId);
    }

    public Collection<User> findAllUsers() {
        return storage.findAllUsers();
    }

    public User createUser(User user) {
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }


    public User findUser(Long userId) throws NoSuchElementException {
        return storage.getUser(userId);
    }
}
