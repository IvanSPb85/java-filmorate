package ru.yandex.practicum.filmorate.storage.dao;

import java.util.Map;

public interface FriendsDao {
    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Map<Long, Boolean> findFriendStatus(Long userId);
}
