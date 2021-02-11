package me.pedrocaires.chapt.core.user;

public class UserRegisterRequest extends UserLoginRequest {

    protected String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
