package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {
    private final UserService userService;

    @Test
    public void createUserTest() {
        userService.createUser(User.builder()
                .birthday(LocalDate.now())
                .name("myName")
                .login("myLogin")
                .email("my@email").build());

        Optional<User> optionalUser = Optional.of(userService.findUser(4L));

        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 4L));
    }

    @Test
    public void findAllUsersTest() {
        Optional<Collection<User>> allUsers = Optional.of(userService.findAllUsers());

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
        Optional<User> filmOptional = Optional.of(userService.updateUser(updateUser));

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "updateName")
                                .hasFieldOrPropertyWithValue("login", "updateLogin"));
    }

    @Test
    public void getUserTest() {
        Optional<User> userOptional = Optional.of(userService.findUser(2L));

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L)
                                .hasFieldOrPropertyWithValue("name", "secondUser"));
    }

    @Test
    public void addFriendAndGetFriendsTests() {
        userService.addFriend(1L, 2L);

        Optional<List<User>> userListOptional = Optional.of(userService.findFriends(1L));

        assertThat(userListOptional).isPresent()
                .hasValueSatisfying(userList ->
                        assertThat(userList).hasSize(1));
    }

    @Test
    public void deleteFriendTest() {
        userService.addFriend(2L, 1L);
        userService.deleteFriend(2L, 1L);

        Optional<List<User>> userListOptional = Optional.of(userService.findFriends(2L));

        assertThat(userListOptional).isPresent()
                .hasValueSatisfying(userList ->
                        assertThat(userList).hasSize(0));
    }

    @Test
    public void getCommonFriendsTest() {
        User friend = userService.createUser(User.builder()
                .birthday(LocalDate.now())
                .name("friend")
                .login("friendLogin")
                .email("friend@email").build());

        userService.addFriend(3L, 5L);
        userService.addFriend(4L, 5L);

        Optional<List<User>> optionalUserList = Optional.of(userService.findCommonFriends(3L, 4L));

        assertThat(optionalUserList).isPresent()
                .hasValueSatisfying(userList ->
                        assertThat(userList).contains(friend));
    }
}
