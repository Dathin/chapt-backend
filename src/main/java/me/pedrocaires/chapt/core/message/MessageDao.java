package me.pedrocaires.chapt.core.message;

import me.pedrocaires.chapt.core.config.MySqlPool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    private static final String INSERT_MESSAGE = "INSERT INTO MESSAGES(`FROM`, `TO`, CONTENT) VALUES (?, ?, ?);";
    private static final String SELECT_LAST_MESSAGES = "SELECT `FROM`, `TO`, CONTENT FROM MESSAGES WHERE (`FROM` = ? AND `TO` = ?) OR (`FROM` = ? AND `TO` = ?) LIMIT ? OFFSET ?";

    public void insert(Integer from, Message message) throws SQLException {
        try (var connection = MySqlPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(INSERT_MESSAGE)) {
                preparedStatement.setInt(1, from);
                preparedStatement.setInt(2, message.getTo());
                preparedStatement.setString(3, message.getContent());
                preparedStatement.execute();
            }
        }
    }

    public List<Message> selectLastMessages(LastMessageRequest lastMessageRequest) throws SQLException {
        try (var connection = MySqlPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(SELECT_LAST_MESSAGES)) {
                preparedStatement.setInt(1, lastMessageRequest.getFrom());
                preparedStatement.setInt(2, lastMessageRequest.getTo());
                preparedStatement.setInt(4, lastMessageRequest.getFrom());
                preparedStatement.setInt(3, lastMessageRequest.getTo());
                preparedStatement.setInt(5, lastMessageRequest.getPageSize());
                preparedStatement.setInt(6, calculateOffset(lastMessageRequest.getPageSize(), lastMessageRequest.getPageIndex()));
                var messages = new ArrayList<Message>();
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        messages.add(new Message(rs.getInt("to"), rs.getString("content")));
                    }
                    return messages;
                }
            }
        }
    }

    private int calculateOffset(int pageSize, int pageIndex) {
        return pageSize * pageIndex;
    }


}
