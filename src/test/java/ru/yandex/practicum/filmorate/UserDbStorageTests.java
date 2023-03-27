package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FriendsDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {
    private final UserDbStorage userDbStorage;
    private final FriendsDaoImpl friendsDao;

    @Test
    public void createUserTest() {
        userDbStorage.createUser(User.builder()
                .birthday(LocalDate.now())
                .name("myName")
                .login("myLogin")
                .email("my@email").build());

        Optional<User> optionalUser = Optional.of(userDbStorage.getUser(4L));

        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 4L));
    }

    @Test
    public void findAllUsersTest() {
        Optional<Collection<User>> allUsers = Optional.of(userDbStorage.findAllUsers());

        assertThat(allUsers).isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films).hasSize(5));
    }

    @Test
    public void updateUserTest() {
        User updateUser = User.builder()
                .id(1L)
                .birthday(LocalDate.now())
                .name("updateName")
                .login("updateLogin")
                .email("update@email").build();
        Optional<User> filmOptional = Optional.of(userDbStorage.updateUser(updateUser));

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "updateName")
                                .hasFieldOrPropertyWithValue("login", "updateLogin"));
    }

    @Test
    public void getUserTest() {
        Optional<User> userOptional = Optional.of(userDbStorage.getUser(2L));

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L)
                                .hasFieldOrPropertyWithValue("name", "secondUser"));
    }

    @Test
    public void addFriendAndGetFriendsTests() {
        userDbStorage.addFriend(1L, 2L);

        Optional<List<User>> userListOptional = Optional.of(userDbStorage.getFriends(1L));

        assertThat(userListOptional).isPresent()
                .hasValueSatisfying(userList ->
                        assertThat(userList).hasSize(1));
    }

    @Test
    public void deleteFriendTest() {
        userDbStorage.addFriend(2L, 1L);
        userDbStorage.deleteFriend(2L, 1L);

        Optional<List<User>> userListOptional = Optional.of(userDbStorage.getFriends(2L));

        assertThat(userListOptional).isPresent()
                .hasValueSatisfying(userList ->
                        assertThat(userList).hasSize(0));
    }

    @Test
    public void getCommonFriendsTest() {
        User friend = userDbStorage.createUser(User.builder()
                .birthday(LocalDate.now())
                .name("friend")
                .login("friendLogin")
                .email("friend@email")
                .friends(friendsDao.findFriends(3L))
                .friendStatus(Map.of()).build());

        userDbStorage.addFriend(3L, 5L);
        userDbStorage.addFriend(4L, 5L);

        Optional<List<User>> optionalUserList = Optional.of(userDbStorage.getCommonFriends(3L, 4L));

        assertThat(optionalUserList).isPresent()
                .hasValueSatisfying(userList ->
                        assertThat(userList).contains(friend));
    }
}
