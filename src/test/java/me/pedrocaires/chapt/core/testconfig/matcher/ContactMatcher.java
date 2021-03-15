package me.pedrocaires.chapt.core.testconfig.matcher;

import me.pedrocaires.chapt.core.contact.Contact;
import org.mockito.ArgumentMatcher;

public class ContactMatcher implements ArgumentMatcher<Contact> {

    private final Contact contact;

    public ContactMatcher(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean matches(Contact contact) {
        return this.contact.getContactId() == contact.getContactId()
                && this.contact.getUserId() == contact.getUserId();
    }
}
