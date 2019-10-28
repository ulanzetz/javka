package ru.naumen.javka.services;

import ru.naumen.javka.exceptions.JavkaException;

public interface FileService {
    byte[] getFile(long userId, String path) throws JavkaException;
}
