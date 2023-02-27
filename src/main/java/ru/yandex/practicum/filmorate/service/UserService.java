package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    public final UserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        List<Long> commonIdFriends = new ArrayList<>(findUser(userId).getFriends());
        commonIdFriends.retainAll(findUser(otherId).getFriends());
        return findUsers(commonIdFriends);
    }

    public List<User> findFriends(Long userId) {
        return findUsers(new ArrayList<>(findUser(userId).getFriends()));
    }

    private List<User> findUsers(List<Long> listId) {
        List<User> users = new ArrayList<>();
        for (Long id : listId) {
            users.add(findUser(id));
        }
        return users;
    }

    public User findUser(Long userId) throws NoSuchElementException {
        return storage.findAllUsers().stream().filter(u -> u.getId() == userId).findFirst().get();
    }
}
