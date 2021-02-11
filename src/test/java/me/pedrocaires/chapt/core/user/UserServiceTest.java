package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.exception.AlreadyExistsException;
import me.pedrocaires.chapt.core.exception.InvalidCredentialsException;
import me.pedrocaires.chapt.core.hash.HashService;
import me.pedrocaires.chapt.core.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserDao userDao;

    UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(userDao);
    }

    @Test
    void shouldInsertNewUser() throws SQLException {
        var userRequest = new UserRegisterRequest();

        userService.insert(userRequest);

        verify(userDao).insert(any(User.class));
    }

    @Test
    void shouldThrowAlreadyExistsException() throws SQLException {
        var userRequest = new UserRegisterRequest();
        doThrow(AlreadyExistsException.class).when(userDao).insert(any());

        assertThrows(AlreadyExistsException.class, () -> userService.insert(userRequest));
    }

    @Test
    void shouldIssueToken() throws SQLException {
        var password = "password";
        var salt = "salt";
        var userLoginRequest = new UserLoginRequest();
        userLoginRequest.setPassword(password);
        var user = new User();
        user.setPassword(HashService.getHash(password, salt));
        user.setSalt(salt);
        when(userDao.getUserLoginInfoByEMail(userLoginRequest)).thenReturn(user);

        var token = userService.login(userLoginRequest);

        JwtService.validateToken(token);
    }

    @Test
    void shouldThrowInvalidCredentialsException() throws SQLException {
        var userLoginRequest = new UserLoginRequest();
        when(userDao.getUserLoginInfoByEMail(userLoginRequest)).thenReturn(new User());

        assertThrows(InvalidCredentialsException.class, () -> userService.login(userLoginRequest));
    }
}
