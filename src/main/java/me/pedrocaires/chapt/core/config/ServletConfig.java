package me.pedrocaires.chapt.core.config;

import me.pedrocaires.chapt.core.constants.GeneralConstant;
import me.pedrocaires.chapt.core.enumerator.ContentType;
import me.pedrocaires.chapt.core.enumerator.HttpHeader;
import me.pedrocaires.chapt.core.enumerator.HttpMethod;
import me.pedrocaires.chapt.core.enumerator.Prefix;
import me.pedrocaires.chapt.core.exception.ExceptionResponse;
import me.pedrocaires.chapt.core.exception.InvalidJwtException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.jwt.JwtService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;

@WebFilter(urlPatterns = {"/*"})
public class ServletConfig implements Filter {

    private static final List<String> UNPROTECTED_ROUTES = Arrays.asList("/user/login", "/user/register", "/chat");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        setDefaultHeaders(httpServletResponse);
        if (isAuthenticated(httpServletRequest, httpServletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void setDefaultHeaders(HttpServletResponse httpServletResponse) {
        httpServletResponse.setContentType(ContentType.JSON.getValue());
        httpServletResponse.setHeader(HttpHeader.ALLOW_ORIGIN.getValue(), GeneralConstant.WILDCARD);
        httpServletResponse.setHeader(HttpHeader.ALLOW_METHODS.getValue(), HttpMethod.getCommaSeparatedValues());
        httpServletResponse.setHeader(HttpHeader.ALLOW_HEADERS.getValue(), HttpHeader.getAllowedHeaders());
    }

    private boolean isAuthenticated(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (!isPreFlightOrUnprotectedRoute(httpServletRequest)) {
            return setAuthentication(httpServletRequest, httpServletResponse);
        }
        return true;
    }

    private boolean setAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String token = httpServletRequest.getHeader(HttpHeader.AUTHORIZATION.getValue());
        if (token == null || !token.startsWith(Prefix.BEARER.getValue())) {
            JsonService.writeJsonObjectToHttpServlet(new ExceptionResponse("Jwt token is missing."), 401, httpServletResponse);
            return false;
        }
        try {
            int userId = JwtService.validateToken(token);
            httpServletRequest.setAttribute(USER_ID, userId);
        } catch (InvalidJwtException ex) {
            JsonService.writeJsonObjectToHttpServlet(ex.toErrorDto(), ex.getStatusCode(), httpServletResponse);
            return false;
        }
        return true;
    }

    private boolean isPreFlightOrUnprotectedRoute(HttpServletRequest httpServletRequest) {
        return UNPROTECTED_ROUTES.contains(httpServletRequest.getServletPath())
                || httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name());
    }

}
