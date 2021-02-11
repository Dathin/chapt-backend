package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.json.JsonService;
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

import static me.pedrocaires.chapt.core.testconfig.MockUtils.mockServletReader;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class UserRegisterControllerTest implements NoParameterConstructorTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    UserService userService;

    UserRegisterController userRegisterController;

    @BeforeEach
    void init() {
        userRegisterController = new UserRegisterController(userService);
    }

    @Test
    void shouldDoCustomPost() throws IOException, SQLException {
        var bodyContent = JsonService.writeJsonObject(new UserRegisterRequest());
        mockServletReader(req, bodyContent);

        var registeredUser = userRegisterController.doCustomPost(req, resp);

        assertNull(registeredUser);
    }

    @Test
    @Override
    public void shouldInstantiateClassWithNoParameters() {
        new UserRegisterController();
    }
}
