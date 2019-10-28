package ru.naumen.javka.services;

import ru.naumen.javka.exceptions.JavkaException;

public interface FileService {
    public byte[] getFile(long userId, String path) throws JavkaException;
}
