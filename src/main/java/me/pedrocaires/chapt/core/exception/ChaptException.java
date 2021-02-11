package me.pedrocaires.chapt.core.exception;

public abstract class ChaptException extends RuntimeException {

    protected final String message;
    protected final int statusCode;

    protected ChaptException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ExceptionResponse toErrorDto() {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(this.message);
        return exceptionResponse;
    }
}
