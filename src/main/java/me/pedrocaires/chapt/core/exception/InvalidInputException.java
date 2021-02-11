package me.pedrocaires.chapt.core.exception;

public class InvalidInputException extends ChaptException {
    public InvalidInputException(String message) {
        super(message, 400);
    }
}
