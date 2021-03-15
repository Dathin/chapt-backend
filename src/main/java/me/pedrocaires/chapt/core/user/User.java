package me.pedrocaires.chapt.core.user;

public class User extends UserRegisterRequest {

    private int id;

    private String salt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

}
