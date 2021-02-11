package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.enumerator.HttpHeader;
import me.pedrocaires.chapt.core.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRefreshTokenControllerTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    UserRefreshTokenController userRefreshTokenController;

    @BeforeEach
    void init() {
        userRefreshTokenController = new UserRefreshTokenController();
    }

    @Test
    void shouldDoCustomPost() {
        when(req.getHeader(HttpHeader.AUTHORIZATION.getValue())).thenReturn(JwtService.issueToken(1));

        var userLoginResponse = userRefreshTokenController.doCustomPost(req, resp);

        assertNotNull(userLoginResponse);

    }
}
