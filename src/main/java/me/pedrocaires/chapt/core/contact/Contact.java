package me.pedrocaires.chapt.core.contact;

public class Contact {
    
    private final int userId;
    
    private final int contactId;

    public Contact(int userId, int contactId) {
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getUserId() {
        return userId;
    }

    public int getContactId() {
        return contactId;
    }

}
