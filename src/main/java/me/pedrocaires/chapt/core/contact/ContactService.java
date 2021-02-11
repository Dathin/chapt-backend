package me.pedrocaires.chapt.core.contact;

import java.sql.SQLException;
import java.util.List;

public class ContactService {

    private final ContactDao contactDao;

    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public List<ContactResponse> getContactsByUserId(int userId) throws SQLException {
        return contactDao.getContactsByUserId(userId);
    }

}
