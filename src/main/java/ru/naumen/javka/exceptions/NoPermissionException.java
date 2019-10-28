package ru.naumen.javka.exceptions;

public class NoPermissionException extends JavkaException {
    public NoPermissionException() {
        super("Нет доступа к данному ресурсу", null);
    }
}
