package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.base.ControllerBase;
import me.pedrocaires.chapt.core.json.JsonService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/register")
public class UserRegisterController extends ControllerBase {

    private final UserService userService;

    public UserRegisterController() {
        this.userService = new UserService(new UserDao());
    }

    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Void doCustomPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        var userRequest = JsonService.readJsonObject(req, UserRegisterRequest.class);
        userService.insert(userRequest);
        return null;
    }


}
