package me.pedrocaires.chapt.core.contact;

import me.pedrocaires.chapt.core.exception.AlreadyExistsException;
import me.pedrocaires.chapt.core.testconfig.matcher.ContactMatcher;
import me.pedrocaires.chapt.core.user.UserRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    ContactDao contactDao;

    @Mock
    List<ContactResponse> contactResponses;

    ContactService contactService;

    @BeforeEach
    void init() {
        this.contactService = new ContactService(contactDao);
    }

    @Test
    void shouldGetContactsByUserId() throws SQLException {
        var userId = 1;
        when(contactDao.getContactsByUserId(userId)).thenReturn(contactResponses);

        var getContactsByUserIdResponse = contactService.getContactsByUserId(userId);

        verify(contactDao).getContactsByUserId(userId);
        assertEquals(contactResponses, getContactsByUserIdResponse);
    }

    @Test
    void shouldAddNewContact() throws SQLException {
        var userId = 1;
        var contactId = 2;
        var contactRequest = new ContactRequest();
        var expectedContact = new Contact(userId ,contactId);
        contactRequest.setContactId(contactId);

        contactService.addContact(userId, contactRequest);

        verify(contactDao).insert(argThat(new ContactMatcher(expectedContact)));
    }

    @Test
    void shouldThrowAlreadyExistsException() throws SQLException {
        var contactRequest = new ContactRequest();
        doThrow(SQLIntegrityConstraintViolationException.class).when(contactDao).insert(any());

        assertThrows(AlreadyExistsException.class, () -> contactService.addContact(1, contactRequest));
    }
}
