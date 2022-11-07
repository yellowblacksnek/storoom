package ru.itmo.highload.storroom.users.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException() {
        super();
    }
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    public ResourceAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
