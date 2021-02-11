package me.pedrocaires.chapt.core.message;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private final MessageDao messageDao;

    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void insert(Integer from, Message message) throws SQLException {
        if (message.getTo() != null) {
            messageDao.insert(from, message);
        }
    }

    public List<LastMessageResponse> selectLastMessages(LastMessageRequest lastMessageRequest) throws SQLException {
        var messages = messageDao.selectLastMessages(lastMessageRequest);
        return parseToLastMessageResponse(messages, lastMessageRequest.getFrom());
    }

    private List<LastMessageResponse> parseToLastMessageResponse(List<Message> messages, Integer from) {
        var lastMessagesResponse = new ArrayList<LastMessageResponse>();
        messages.forEach(message -> lastMessagesResponse.add(message.toLastMessageResponse(from)));
        return lastMessagesResponse;
    }

}
