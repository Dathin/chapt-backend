package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.base.ControllerBase;
import me.pedrocaires.chapt.core.enumerator.HttpHeader;
import me.pedrocaires.chapt.core.jwt.JwtService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user/refresh")
public class UserRefreshTokenController extends ControllerBase {

    @Override
    @SuppressWarnings("unchecked")
    public UserLoginResponse doCustomPost(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getHeader(HttpHeader.AUTHORIZATION.getValue());
        String refreshedToken = JwtService.refreshToken(token);
        return new UserLoginResponse(refreshedToken);
    }

}
