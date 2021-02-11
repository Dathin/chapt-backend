package me.pedrocaires.chapt.core.message;

import me.pedrocaires.chapt.core.base.ControllerBase;
import me.pedrocaires.chapt.core.exception.InvalidInputException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static me.pedrocaires.chapt.core.constants.MessageConstant.BE_VALID_NUMBER;
import static me.pedrocaires.chapt.core.constants.MessageConstant.INVALID_FIELD;
import static me.pedrocaires.chapt.core.constants.ParameterConstant.*;

@WebServlet("/message")
public class MessageController extends ControllerBase {

    private final MessageService messageService;

    public MessageController() {
        this.messageService = new MessageService(new MessageDao());
    }

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LastMessageResponse> doCustomGet(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        var lastMessageRequest = validatedSelectLastMessageInput(req);
        return messageService.selectLastMessages(lastMessageRequest);
    }

    private LastMessageRequest validatedSelectLastMessageInput(HttpServletRequest req) {
        String message = "";
        var lastMessageRequest = new LastMessageRequest();
        try {
            lastMessageRequest.setPageSize(Integer.parseInt(req.getParameter(PAGE_SIZE)));
        } catch (NumberFormatException ex) {
            message += String.format(INVALID_FIELD, PAGE_SIZE, BE_VALID_NUMBER);
        }
        try {
            lastMessageRequest.setPageIndex(Integer.parseInt(req.getParameter(PAGE_INDEX)));
        } catch (NumberFormatException ex) {
            message += String.format(INVALID_FIELD, PAGE_INDEX, BE_VALID_NUMBER);
        }
        try {
            lastMessageRequest.setTo(Integer.parseInt(req.getParameter(TO)));
        } catch (NumberFormatException ex) {
            message += String.format(INVALID_FIELD, TO, BE_VALID_NUMBER);
        }
        lastMessageRequest.setFrom(getUserId(req));
        if (!message.isEmpty()) {
            throw new InvalidInputException(message);
        } else {
            return lastMessageRequest;
        }
    }

}
