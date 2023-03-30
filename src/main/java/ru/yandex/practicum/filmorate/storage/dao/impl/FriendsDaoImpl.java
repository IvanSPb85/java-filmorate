package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String ADD_FRIEND = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    private static final String FIND_FRIENDS = "SELECT friend_id FROM friends WHERE user_id = ?";

    private static final String EXISTS_FRIENDSHIP = "SELECT COUNT(*) FROM friends " +
            "WHERE user_id = ? AND friend_id = ?";

    @Autowired
    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update(ADD_FRIEND, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        jdbcTemplate.update(DELETE_FRIEND, userId, friendId);
    }

    private Set<Long> findFriends(Long userId) {
        return new HashSet<>(jdbcTemplate.query(FIND_FRIENDS, (rs, rowNum) ->
                (rs.getLong("friend_id")), userId));
    }

    @Override
    public Map<Long, Boolean> findFriendStatus(Long userId) {
        Map<Long, Boolean> status = new HashMap<>();
        for (Long friend : findFriends(userId)) {
            status.put(friend, existsFriendship(friend, userId));
        }
        return status;
    }

    private boolean existsFriendship(Long userId, Long friendId) {
        int result = jdbcTemplate.queryForObject(EXISTS_FRIENDSHIP, Integer.class, userId, friendId);
        return result == 1;
    }
}
