package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final UserValidation validation;
    private Long counterId;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        validation.isValid(user);
        getNextId();
        user.setId(counterId);
        users.put(counterId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else throw new NoSuchElementException("Данный пользователь не найден!");
        return user;
    }

    @Override
    public User getUser(Long userId) {
        return findAllUsers().stream().filter(u -> u.getId() == userId).findFirst().get();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return findUsers(new ArrayList<>(getUser(userId).getFriends()));
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<Long> commonIdFriends = new ArrayList<>(getUser(userId).getFriends());
        commonIdFriends.retainAll(getUser(friendId).getFriends());
        return findUsers(commonIdFriends);
    }

    private List<User> findUsers(List<Long> listId) {
        List<User> users = new ArrayList<>();
        for (Long id : listId) {
            users.add(getUser(id));
        }
        return users;
    }

    private void getNextId() {
        counterId++;
    }
}
