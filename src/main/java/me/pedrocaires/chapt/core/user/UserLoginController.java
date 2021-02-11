package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.base.ControllerBase;
import me.pedrocaires.chapt.core.json.JsonService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/login")
public class UserLoginController extends ControllerBase {

    private final UserService userService;

    public UserLoginController() {
        this.userService = new UserService(new UserDao());
    }

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserLoginResponse doCustomPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        UserLoginRequest userLoginRequest = JsonService.readJsonObject(req, UserLoginRequest.class);
        var token = userService.login(userLoginRequest);
        return new UserLoginResponse(token);
    }

}
