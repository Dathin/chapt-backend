package me.pedrocaires.chapt.core.contact;

import me.pedrocaires.chapt.core.config.MySqlPool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    private static final String SELECT_CONTACTS = "SELECT U.ID, U.E_MAIL FROM CONTACTS AS C INNER JOIN USERS U on C.CONTACT_ID = U.ID WHERE USER_ID = ?";

    private static final String INSERT_CONTACT = "INSERT INTO CONTACTS (USER_ID, CONTACT_ID) VALUES (?, ?)";

    public List<ContactResponse> getContactsByUserId(int userId) throws SQLException {
        try (var connection = MySqlPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(SELECT_CONTACTS)) {
                preparedStatement.setInt(1, userId);
                List<ContactResponse> contactResponses;
                try (var rs = preparedStatement.executeQuery()) {
                    contactResponses = new ArrayList<>();
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
    }

    public void insert(Contact contact) throws SQLException {
        try(var connection = MySqlPool.getConnection()){
            try(var preparedStatement = connection.prepareStatement(INSERT_CONTACT)){
                preparedStatement.setInt(1, contact.getUserId());
                preparedStatement.setInt(2, contact.getContactId());
                preparedStatement.execute();
            }
        }
    }
}
