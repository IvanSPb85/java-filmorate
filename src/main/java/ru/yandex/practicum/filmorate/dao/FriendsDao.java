package ru.yandex.practicum.filmorate.dao;

public interface FriendsDao {
    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);
}
