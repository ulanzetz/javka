package ru.naumen.javka.exceptions;

public abstract class JavkaException extends Exception {
    JavkaException(String message, Throwable cause) {
        super(message, cause);
    }
}
