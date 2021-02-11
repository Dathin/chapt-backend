package me.pedrocaires.chapt.core.exception;

public class InvalidJwtException extends ChaptException {

    public InvalidJwtException() {
        super("Your authentication is not valid.", 401);
    }
}
