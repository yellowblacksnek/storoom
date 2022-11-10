package ru.itmo.highload.storoom.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource not found!");
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entityName, UUID entityId) {
        super(entityName + " " + entityId + " not found");
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
