package ru.naumen.javka.services;

import ru.naumen.javka.exceptions.GetFileException;
import ru.naumen.javka.exceptions.JavkaException;
import ru.naumen.javka.storage.FileStorage;

import java.io.IOException;

public class FileServiceImpl implements FileService {
    private FileStorage storage;

    public FileServiceImpl(FileStorage storage) {
        this.storage = storage;
    }

    @Override
    public byte[] getFile(long userId, String path) throws JavkaException {
        try {
            return storage.getFile(path);
        } catch (IOException io) {
            throw new GetFileException(path, io);
        }
    }
}
