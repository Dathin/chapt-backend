package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.testinterface.NoParameterConstructorTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static me.pedrocaires.chapt.core.testconfig.mock.MockUtils.mockServletReader;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserLoginControllerTest implements NoParameterConstructorTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    UserService userService;

    UserLoginController userLoginController;

    @BeforeEach
    void init() {
        userLoginController = new UserLoginController(userService);
    }

    @Test
    void shouldDoCustomPost() throws IOException, SQLException {
        mockServletReader(req, new UserLoginRequest());

        var userLoginResponse = userLoginController.doCustomPost(req, resp);

        assertNotNull(userLoginResponse);
    }

    @Test
    @Override
    public void shouldInstantiateClassWithNoParameters() {
        new UserLoginController();
    }
}
