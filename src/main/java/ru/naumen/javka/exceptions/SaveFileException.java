package ru.naumen.javka.exceptions;

public class SaveFileException extends JavkaException{
    public SaveFileException(String path, Throwable cause) {
        super(String.format("Не удалось создать файл %s", path), cause);
    }
}
