package me.pedrocaires.chapt.core.contact;

import me.pedrocaires.chapt.core.base.ControllerBase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;

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
        var userId = (int) req.getAttribute(USER_ID);
        return contactService.getContactsByUserId(userId);
    }
}
