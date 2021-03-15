package me.pedrocaires.chapt.core.contact;

import me.pedrocaires.chapt.core.testinterface.NoParameterConstructorTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;
import static me.pedrocaires.chapt.core.testconfig.mock.MockUtils.mockServletAttribute;
import static me.pedrocaires.chapt.core.testconfig.mock.MockUtils.mockServletReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest implements NoParameterConstructorTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    ContactService contactService;

    @Mock
    List<ContactResponse> contactResponses;

    ContactController contactController;

    @BeforeEach
    void init() {
        this.contactController = new ContactController(contactService);
    }

    @Test
    void shouldReturnContactsByUserId() throws SQLException {
        var userId = 1;
        mockServletAttribute(req, USER_ID, userId);
        when(contactService.getContactsByUserId(userId)).thenReturn(contactResponses);

        var userContacts = contactController.doCustomGet(req, resp);

        assertEquals(contactResponses, userContacts);
    }

    @Test
    void shouldAddNewContact() throws SQLException, IOException {
        var userId = 1;
        mockServletAttribute(req, USER_ID, userId);
        mockServletReader(req, new ContactRequest());

        var contactAdded = contactController.doCustomPost(req, resp);

        assertNull(contactAdded);
    }

    @Test
    @Override
    public void shouldInstantiateClassWithNoParameters() {
        new ContactController();
    }
}
