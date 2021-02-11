package me.pedrocaires.chapt.core.message;

public class LastMessageResponse {

    private boolean sent;

    private String content;

    public LastMessageResponse() {
    }

    public LastMessageResponse(boolean sent, String content) {
        this.sent = sent;
        this.content = content;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
