package me.pedrocaires.chapt.core.exception;

public class MethodNotAllowed extends ChaptException {

    public MethodNotAllowed() {
        super("Method Not Allowed", 405);
    }

}
