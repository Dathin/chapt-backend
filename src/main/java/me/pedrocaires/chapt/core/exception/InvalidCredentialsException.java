package me.pedrocaires.chapt.core.exception;

public class InvalidCredentialsException extends ChaptException {
    public InvalidCredentialsException() {
        super("Invalid credentials.", 400);
    }
}
