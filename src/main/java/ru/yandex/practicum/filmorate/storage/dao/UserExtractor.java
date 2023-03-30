package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserExtractor implements ResultSetExtractor<Map<User, List<Long>>> {
    @Override
    public Map<User, List<Long>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<User, List<Long>> users = new HashMap<>();

        while (rs.next()) {
            User user = User.builder()
                    .id(rs.getLong(1))
                    .email(rs.getString(2))
                    .login(rs.getString(3))
                    .name(rs.getString(4))
                    .birthday(rs.getDate(5).toLocalDate()).build();

            users.putIfAbsent(user, new ArrayList<>());
            Long friendId = rs.getLong(6);
            if (friendId != 0) users.get(user).add(friendId);
        }
        return users;
    }
}
