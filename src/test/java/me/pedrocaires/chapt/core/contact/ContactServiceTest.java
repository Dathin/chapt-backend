package me.pedrocaires.chapt.core.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
