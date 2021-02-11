package me.pedrocaires.chapt.core.base;

import me.pedrocaires.chapt.core.enumerator.HttpMethod;
import me.pedrocaires.chapt.core.exception.ChaptException;
import me.pedrocaires.chapt.core.exception.MethodNotAllowed;
import me.pedrocaires.chapt.core.exception.UnexpectedException;
import me.pedrocaires.chapt.core.json.JsonService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;

public abstract class ControllerBase extends HttpServlet {

    public <T> T doCustomPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        throw new MethodNotAllowed();
    }

    public <T> T doCustomGet(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        throw new MethodNotAllowed();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        handleRequest(req, resp, HttpMethod.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        handleRequest(req, resp, HttpMethod.POST);
    }

    protected int getUserId(HttpServletRequest req) {
        return (int) req.getAttribute(USER_ID);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp, HttpMethod httpMethod) {
        try {
            try {
                writeSuccessObject(getEndpointResponse(req, resp, httpMethod), resp);
            } catch (ChaptException ex) {
                ex.printStackTrace();
                catchChaptException(ex, resp);
            } catch (Exception ex) {
                ex.printStackTrace();
                catchUnexpectedException(resp);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new UnexpectedException();
        }
    }

    private Object getEndpointResponse(HttpServletRequest req, HttpServletResponse resp, HttpMethod httpMethod) throws SQLException, IOException {
        switch (httpMethod) {
            case GET:
                return doCustomGet(req, resp);
            case POST:
                return doCustomPost(req, resp);
            default:
                throw new MethodNotAllowed();
        }
    }

    private void writeSuccessObject(Object object, HttpServletResponse resp) throws IOException {
        JsonService.writeJsonObjectToHttpServlet(object, 200, resp);
    }

    private void catchChaptException(ChaptException ex, HttpServletResponse resp) throws IOException {
        JsonService.writeJsonObjectToHttpServlet(ex.toErrorDto(), ex.getStatusCode(), resp);
    }

    private void catchUnexpectedException(HttpServletResponse resp) throws IOException {
        JsonService.writeJsonObjectToHttpServlet(new UnexpectedException().toErrorDto(), 500, resp);
    }

}
