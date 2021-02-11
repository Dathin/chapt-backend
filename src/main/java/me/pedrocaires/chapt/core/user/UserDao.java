package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.config.MySqlPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {

    private static final String INSERT_USER = "INSERT INTO USERS(E_MAIL, PASSWORD, SALT, PHOTO) VALUES (?, ?, ?, ?);";
    private static final String SELECT_USER_BY_EMAIL = "SELECT ID, E_MAIL, PASSWORD, SALT FROM USERS WHERE E_MAIL = ? LIMIT 1";

    public void insert(User user) throws SQLException {
        try (var connection = MySqlPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(INSERT_USER)) {
                preparedStatement.setString(1, user.geteMail());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getSalt());
                preparedStatement.setString(4, user.getPhoto());
                preparedStatement.execute();
            }
        }
    }

    public User getUserLoginInfoByEMail(UserLoginRequest userLoginRequest) throws SQLException {
        try (var connection = MySqlPool.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {
                preparedStatement.setString(1, userLoginRequest.geteMail());
                try (var rs = preparedStatement.executeQuery()) {
                    var user = new User();
                    while (rs.next()) {
                        user.setId(rs.getInt("ID"));
                        user.seteMail(rs.getString("E_MAIL"));
                        user.setPassword(rs.getString("PASSWORD"));
                        user.setSalt(rs.getString("SALT"));
                    }
                    return user;
                }
            }
        }
    }

}
