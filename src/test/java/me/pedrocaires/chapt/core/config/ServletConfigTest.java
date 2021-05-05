package me.pedrocaires.chapt.core.config;

import me.pedrocaires.chapt.core.constants.GeneralConstant;
import me.pedrocaires.chapt.core.enumerator.ContentType;
import me.pedrocaires.chapt.core.enumerator.HttpHeader;
import me.pedrocaires.chapt.core.enumerator.HttpMethod;
import me.pedrocaires.chapt.core.enumerator.Prefix;
import me.pedrocaires.chapt.core.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;
import static me.pedrocaires.chapt.core.testconfig.assertion.Assertions.assertServletStatusCode;
import static me.pedrocaires.chapt.core.testconfig.assertion.Assertions.assertServletWroteObject;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServletConfigTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    FilterChain filterChain;

    @Mock
    PrintWriter printWriter;

    String shouldNotAuthenticatePath = "/user/login";

    ServletConfig servletConfig;

    @BeforeEach
    void init() {
        this.servletConfig = new ServletConfig();
    }

    @Test
    void shouldCallDoFilter() throws IOException, ServletException {
        String validToken = Prefix.BEARER.getValue() + JwtService.issueToken(1);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());
        when(req.getHeader(HttpHeader.AUTHORIZATION.getValue())).thenReturn(validToken);

        servletConfig.doFilter(req, resp, filterChain);

        verify(filterChain).doFilter(req, resp);
    }

    @Test
    void shouldNotCallDoFilterWhenUnauthorized() throws IOException, ServletException {
        when(resp.getWriter()).thenReturn(printWriter);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);

        verify(filterChain, never()).doFilter(req, resp);
    }

    @Test
    void shouldSetDefaultHeaders() throws IOException, ServletException {
        when(resp.getWriter()).thenReturn(printWriter);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);


        verify(resp).setContentType(ContentType.JSON.getValue());
        verify(resp).setHeader(HttpHeader.ALLOW_ORIGIN.getValue(), GeneralConstant.WILDCARD);
        verify(resp).setHeader(HttpHeader.ALLOW_METHODS.getValue(), HttpMethod.getCommaSeparatedValues());
        verify(resp).setHeader(HttpHeader.ALLOW_HEADERS.getValue(), HttpHeader.getAllowedHeaders());
    }

    @Test
    void shouldInformWhenMalformedToken() throws IOException, ServletException {
        var invalidToken = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJleHAiOjE2MDY3OTc3Nzl9.Q5sKWROwbq-sz4vE9y4aHF2_Owkvj0vXMvjRh50Iw88_dDmdCGcSgzHgJTuogVUW";
        var jsonRepresentation = "{\"message\":\"Your authentication is not valid.\"}";
        when(req.getHeader(HttpHeader.AUTHORIZATION.getValue())).thenReturn(invalidToken);
        when(resp.getWriter()).thenReturn(printWriter);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);

        assertServletStatusCode(resp, 401);
        assertServletWroteObject(printWriter, jsonRepresentation);
    }

    @Test
    void shouldInformWhenExpiredToken() throws IOException, ServletException {
        var expiredToken = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJleHAiOjE2MDY3OTc3Nzl9.Q5sKWROwbq-sz4vE9y4aHF2_Owkvj0vXMvjRh50Iw88_dDmdCGcSgzHgJTuogVUW";
        var jsonRepresentation = "{\"message\":\"Your authentication is not valid.\"}";
        when(req.getHeader(HttpHeader.AUTHORIZATION.getValue())).thenReturn(expiredToken);

        when(resp.getWriter()).thenReturn(printWriter);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);

        assertServletStatusCode(resp, 401);
        assertServletWroteObject(printWriter, jsonRepresentation);
    }

    @Test
    void shouldInformWhenAuthorizationIsNull() throws IOException, ServletException {
        var jsonRepresentation = "{\"message\":\"Jwt token is missing.\"}";
        when(resp.getWriter()).thenReturn(printWriter);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);

        assertServletStatusCode(resp, 401);
        assertServletWroteObject(printWriter, jsonRepresentation);
    }

    @Test
    void shouldInformWhenAuthorizationIsMissingBearer() throws IOException, ServletException {
        var missingBearerToken = "NoBearer ";
        var jsonRepresentation = "{\"message\":\"Jwt token is missing.\"}";
        when(req.getHeader(HttpHeader.AUTHORIZATION.getValue())).thenReturn(missingBearerToken);
        when(resp.getWriter()).thenReturn(printWriter);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);

        assertServletStatusCode(resp, 401);
        assertServletWroteObject(printWriter, jsonRepresentation);
    }

    @Test
    void shouldAuthenticate() throws IOException, ServletException {
        var validToken = Prefix.BEARER.getValue() + JwtService.issueToken(1);
        when(req.getHeader(HttpHeader.AUTHORIZATION.getValue())).thenReturn(validToken);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);

        verify(resp, never()).setStatus(anyInt());
        verify(printWriter, never()).flush();
    }

    @Test
    void shouldAddUserIdAttributeWhenLoggedIn() throws IOException, ServletException {
        int userId = 1;
        String validToken = Prefix.BEARER.getValue() + JwtService.issueToken(userId);
        when(req.getHeader(HttpHeader.AUTHORIZATION.getValue())).thenReturn(validToken);
        when(req.getMethod()).thenReturn(HttpMethod.POST.name());

        servletConfig.doFilter(req, resp, filterChain);

        verify(req).setAttribute(USER_ID, userId);
    }

    @Test
    void shouldNotAuthenticateWhenPreflightRequest() throws IOException, ServletException {
        when(req.getMethod()).thenReturn(HttpMethod.OPTIONS.name());

        servletConfig.doFilter(req, resp, filterChain);

        verify(req, never()).setAttribute(any(), any());
    }

    @Test
    void shouldNotAuthenticateWhenUnprotectedRoute() throws IOException, ServletException {
        when(req.getServletPath()).thenReturn(shouldNotAuthenticatePath);

        servletConfig.doFilter(req, resp, filterChain);

        verify(req, never()).setAttribute(any(), any());
    }

}
