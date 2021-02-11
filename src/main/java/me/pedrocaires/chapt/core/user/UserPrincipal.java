package me.pedrocaires.chapt.core.user;

public class UserPrincipal {

    private int userId;

    private boolean isAuthenticated;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

}
