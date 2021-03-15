package me.pedrocaires.chapt.core.contact;

import me.pedrocaires.chapt.core.base.ControllerBase;
import me.pedrocaires.chapt.core.json.JsonService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/contact")
public class ContactController extends ControllerBase {

    private final ContactService contactService;

    public ContactController() {
        this.contactService = new ContactService(new ContactDao());
    }

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContactResponse> doCustomGet(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        var userId = getUserId(req);
        return contactService.getContactsByUserId(userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Void doCustomPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        var userId = getUserId(req);
        ContactRequest contactRequest = JsonService.readJsonObject(req, ContactRequest.class);
        contactService.addContact(userId, contactRequest);
        return null;
    }
}
