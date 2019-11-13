package ru.naumen.javka.exceptions;

public class ValidationException extends JavkaException {
    public ValidationException(String message) {
        super(message, null);
    }
}
