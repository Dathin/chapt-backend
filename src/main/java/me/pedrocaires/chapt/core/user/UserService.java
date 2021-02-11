package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.exception.AlreadyExistsException;
import me.pedrocaires.chapt.core.exception.InvalidCredentialsException;
import me.pedrocaires.chapt.core.hash.HashService;
import me.pedrocaires.chapt.core.jwt.JwtService;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void insert(UserRegisterRequest userRequest) throws SQLException {
        User user = new User();
        user.seteMail(userRequest.geteMail());
        user.setPhoto(userRequest.getPhoto());
        String salt = HashService.getSalt();
        user.setSalt(salt);
        user.setPassword(HashService.getHash(userRequest.getPassword(), salt));
        try {
            userDao.insert(user);
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new AlreadyExistsException("E-mail");
        }
    }

    public String login(UserLoginRequest userLoginRequest) throws SQLException {
        User user = userDao.getUserLoginInfoByEMail(userLoginRequest);
        String currentHash = HashService.getHash(userLoginRequest.getPassword(), user.getSalt());
        if (!currentHash.equals(user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return JwtService.issueToken(user.getId());
    }

}
