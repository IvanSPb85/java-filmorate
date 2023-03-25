package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FriendsDao;

import java.util.List;

public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "SELECT INTO friends(user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<Long> findAllFriends(Long userId) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rawNum) -> (rs.getLong("user_id")), userId);
    }

    @Override
    public List<Long> findCommonFriends(Long userId, Long friendId) {
        String sql = "SELECT ff.friend_id FROM friends AS f WHERE f.user_id = ? AND f.friend_id <> ?" +
                " INNER JOIN friends AS ff ON f.friend_id = ff.friend_id WHERE ff.user_id = ? AND ff.friend_id <> ?";
        return jdbcTemplate.query(sql, (rs, rawNum) -> (rs.getLong("friend_id")), userId, friendId, friendId, userId);
    }
}
