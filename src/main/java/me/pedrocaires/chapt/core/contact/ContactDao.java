package me.pedrocaires.chapt.core.contact;

import me.pedrocaires.chapt.core.config.MySqlPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private static final String SELECT_CONTACTS = "SELECT U.ID, U.E_MAIL FROM CONTACTS AS C INNER JOIN USERS U on C.CONTACT_ID = U.ID WHERE USER_ID = ?";

    public List<ContactResponse> getContactsByUserId(int userId) throws SQLException {
        try (var connection = MySqlPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTACTS);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            List<ContactResponse> contactResponses = new ArrayList<>();
            while (rs.next()) {
                ContactResponse contactResponse = new ContactResponse();
                contactResponse.setId(rs.getInt("ID"));
                contactResponse.seteMail(rs.getString("E_MAIL"));
                contactResponses.add(contactResponse);
            }
            return contactResponses;
        }
    }
}
