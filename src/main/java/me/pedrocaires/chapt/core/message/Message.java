package me.pedrocaires.chapt.core.message;

public class Message {

    private Integer to;

    private String content;

    public Message() {
    }

    public Message(Integer to, String content) {
        this.to = to;
        this.content = content;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LastMessageResponse toLastMessageResponse(Integer from) {
        return new LastMessageResponse(!from.equals(this.to), content);
    }

}
