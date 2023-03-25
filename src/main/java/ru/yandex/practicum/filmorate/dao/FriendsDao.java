package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {
    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<Long> findAllFriends(Long userId);

    List<Long> findCommonFriends(Long userId, Long friendId);
}
