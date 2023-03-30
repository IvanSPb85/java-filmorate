package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;
    private final UserValidation validation;
    private final FriendsDao friendsDao;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage,
                       UserValidation validation, FriendsDao friendsDao) {
        this.storage = storage;
        this.validation = validation;
        this.friendsDao = friendsDao;
    }

    public void addFriend(Long userId, Long friendId) {
        storage.addFriend(userId, friendId);
        friendsDao.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        storage.deleteFriend(userId, friendId);
        friendsDao.deleteFriend(userId, friendId);
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        return storage.getCommonFriends(userId, otherId);
    }

    public List<User> findFriends(Long userId) {
        return storage.getFriends(userId);
    }

    public Collection<User> findAllUsers() {
        Collection<User> users = storage.findAllUsers();
        users.forEach(user -> user.getFriendStatus().putAll(friendsDao.findFriendStatus(user.getId())));
        return users;
    }

    public User createUser(User user) {
        validation.isValid(user);
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public User findUser(Long userId) throws NoSuchElementException {
        User user = storage.getUser(userId);
        user.getFriendStatus().putAll(friendsDao.findFriendStatus(userId));
        return user;
    }
}
