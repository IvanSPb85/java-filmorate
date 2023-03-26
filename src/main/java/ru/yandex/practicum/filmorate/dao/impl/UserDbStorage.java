package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserValidation validation;
    private final FriendsDao friendsDao;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserValidation validation, FriendsDao friendsDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.validation = validation;
        this.friendsDao = friendsDao;
    }

    @Override
    public Collection<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User createUser(User user) {
        validation.isValid(user);
        String sqlQuery = "INSERT INTO users(email, login, name, birthday) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
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
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUser(user.getId());
    }

    @Override
    public User getUser(Long userId) {
        existsUser(userId);
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToUser, userId);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        existsUser(friendId);
        existsUser(userId);
        friendsDao.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        existsUser(friendId);
        existsUser(userId);
        friendsDao.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        String sql = "SELECT * FROM users AS u " +
                "INNER JOIN friends AS f ON u.user_id = f.friend_id WHERE f.user_id = " + userId;
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        String sql = "SELECT * FROM users AS u WHERE u.user_id IN (" +
                "SELECT friend_id FROM friends WHERE user_id = " + userId +
                ") AND u.user_id IN (SELECT friend_id FROM friends WHERE user_id = " + friendId + ")";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    private User mapRowToUser(ResultSet rs, int rawNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate()).build();
    }

    private boolean existsUser(long userID) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, userID);
        if (result != 1) {
            throw new NoSuchElementException("В базе не найден пользователь с Id = " + userID);
        }
        return true;
    }
}
