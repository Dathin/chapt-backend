package me.pedrocaires.chapt.core.exception;

public class AlreadyExistsException extends ChaptException {

    public AlreadyExistsException(String field) {
        super(String.format("%s already exists.", field), 400);
    }

}
