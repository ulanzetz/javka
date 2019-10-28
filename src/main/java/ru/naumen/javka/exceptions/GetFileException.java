package ru.naumen.javka.exceptions;

public class GetFileException extends JavkaException {
    public GetFileException(String path, Throwable cause) {
        super(String.format("Не удалось открыть файл %s", path), cause);
    }
}
