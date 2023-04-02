package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserExtractor;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserExtractor userExtractor;
    private static final String FIND_ALL_USERS = "SELECT u.user_id, u.email, u.login, " +
            "u.name, u.birthday, f.friend_id FROM users AS u " +
            "LEFT JOIN friends AS f ON u.user_Id = f.user_id " +
            "GROUP BY u.user_id, f.friend_id";
    private static final String CREATE_USER = "INSERT INTO users(email, login, name, birthday) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
            " WHERE user_id = ?";
    private static final String GET_USER = "SELECT u.user_id, u.email, u.login, " +
            "u.name, u.birthday, f.friend_id FROM users AS u " +
            "LEFT JOIN friends AS f ON u.user_Id = f.user_id " +
            "WHERE u.user_id = ? " +
            "GROUP BY u.user_id, f.friend_id";
    private static final String GET_FRIENDS = "SELECT * FROM users AS u " +
            "INNER JOIN friends AS f ON u.user_id = f.friend_id WHERE f.user_id = ?";
    private static final String GET_COMMON_FRIENDS = "SELECT * FROM users AS u WHERE u.user_id IN (" +
            "SELECT friend_id FROM friends WHERE user_id = ?) " +
            "AND u.user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String EXISTS_USER = "SELECT COUNT(*) FROM users WHERE user_id = ?";

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserExtractor userExtractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.userExtractor = userExtractor;
    }

    @Override
    public Collection<User> findAllUsers() {
        Map<User, List<Long>> userListMap = jdbcTemplate.query(FIND_ALL_USERS, userExtractor);
        userListMap.forEach(User::setFriends);
        return userListMap.keySet();
    }

    @Override
    public User createUser(User user) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_USER, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        Long userId = keyHolder.getKey().longValue();
        user.setId(userId);
        return user;
    }

    @Override
    public User updateUser(User user) {
        existsUser(user.getId());
        jdbcTemplate.update(UPDATE_USER, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUser(Long userId) {
        existsUser(userId);
        Map<User, List<Long>> users = jdbcTemplate.query(GET_USER, userExtractor, userId);
        users.forEach(User::setFriends);
        return users.keySet().stream().findFirst().get();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        existsUser(friendId);
        existsUser(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        existsUser(friendId);
        existsUser(userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return jdbcTemplate.query(GET_FRIENDS, this::mapRowToUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return jdbcTemplate.query(GET_COMMON_FRIENDS, this::mapRowToUser, userId, friendId);
    }

    private User mapRowToUser(ResultSet rs, int rawNum) throws SQLException {
        Long userId = rs.getLong("user_id");
        return User.builder()
                .id(userId)
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate()).build();
    }

    private boolean existsUser(long userID) {
        int result = jdbcTemplate.queryForObject(EXISTS_USER, Integer.class, userID);
        if (result != 1) {
            throw new NoSuchElementException("В базе не найден пользователь с Id = " + userID);
        }
        return true;
    }
}
