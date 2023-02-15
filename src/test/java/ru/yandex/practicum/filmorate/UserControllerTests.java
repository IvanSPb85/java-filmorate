package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {
    UserController controller = new UserController();
    User user;

    @BeforeEach
    public void createUser() {
        user = User.builder()
                .email("email@com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();
    }

    @Test
    public void isValidUserNotThrowValidateExceptionTest() {
        controller.isValid(user);
    }

    @Test
    public void isValidUserEmailFailThrowValidateExceptionTest() {
        user.setEmail("email");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void isValidUserEmailNullThrowValidateExceptionTest() {
        user.setEmail(null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void isValidUserEmailEmptyThrowValidateExceptionTest() {
        user.setEmail(" ");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void isValidUserLoginNullThrowValidateExceptionTest() {
        user.setLogin(null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void isValidUserLoginEmptyThrowValidateExceptionTest() {
        user.setLogin(" ");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void isValidUserLoginFailThrowValidateExceptionTest() {
        user.setLogin("login login");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void isValidUserNameEmptyThrowValidateExceptionTest() {
        user.setName(" ");
        controller.isValid(user);
        assertEquals("login", user.getName(),
                "при пустом имени должен использоваться логин");
    }

    @Test
    public void isValidUserNameNullThrowValidateExceptionTest() {
        user.setName(null);
        controller.isValid(user);
        assertEquals("login", user.getName(),
                "при пустом имени должен использоваться логин");
    }

    @Test
    public void isValidUserFailBirthdayThrowValidateExceptionTest() {
        user.setBirthday(LocalDate.now().plusDays(1));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.isValid(user));
        assertEquals("дата рождения не может быть в будущем", exception.getMessage());
    }
}
