package me.pedrocaires.chapt.core.contact;

import me.pedrocaires.chapt.core.exception.AlreadyExistsException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ContactService {

    private final ContactDao contactDao;

    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public List<ContactResponse> getContactsByUserId(int userId) throws SQLException {
        return contactDao.getContactsByUserId(userId);
    }

    public void addContact(int userId, ContactRequest contactRequest) throws SQLException {
        try {
            contactDao.insert(new Contact(userId, contactRequest.getContactId()));
        }  catch (SQLIntegrityConstraintViolationException ex) {
            throw new AlreadyExistsException("Contact");
        }
    }

}
