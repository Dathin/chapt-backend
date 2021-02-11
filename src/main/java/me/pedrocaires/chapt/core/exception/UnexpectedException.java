package me.pedrocaires.chapt.core.exception;

public class UnexpectedException extends ChaptException {
    public UnexpectedException() {
        super("An unexpected error occurred, please try again ou contact us.", 500);
    }
}
