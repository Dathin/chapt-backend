package me.pedrocaires.chapt.core.message;

import me.pedrocaires.chapt.core.base.ControllerBase;
import me.pedrocaires.chapt.core.constants.MessageConstant;
import me.pedrocaires.chapt.core.constants.ParameterConstant;
import me.pedrocaires.chapt.core.exception.InvalidInputException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

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
            lastMessageRequest.setPageSize(Integer.parseInt(req.getParameter(ParameterConstant.PAGE_SIZE)));
        } catch (NumberFormatException ex) {
            message += String.format(MessageConstant.INVALID_FIELD, ParameterConstant.PAGE_SIZE, "be a valid number");
        }
        try {
            lastMessageRequest.setPageIndex(Integer.parseInt(req.getParameter(ParameterConstant.PAGE_INDEX)));
        } catch (NumberFormatException ex) {
            message += String.format(MessageConstant.INVALID_FIELD, ParameterConstant.PAGE_INDEX, "be a valid number");
        }
        try {
            lastMessageRequest.setTo(Integer.parseInt(req.getParameter(ParameterConstant.TO)));
        } catch (NumberFormatException ex) {
            message += String.format(MessageConstant.INVALID_FIELD, ParameterConstant.TO, "be a valid number");
        }
        lastMessageRequest.setFrom(getUserId(req));
        if (!message.isEmpty()) {
            throw new InvalidInputException(message);
        } else {
            return lastMessageRequest;
        }
    }

}
