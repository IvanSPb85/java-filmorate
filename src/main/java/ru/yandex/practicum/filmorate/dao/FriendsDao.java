package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FriendsDao {
    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Set<Long> findFriends(Long userID);

    boolean existsFriendship(Long userId, Long friendId);
}
