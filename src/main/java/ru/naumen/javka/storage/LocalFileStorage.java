package ru.naumen.javka.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFileStorage implements FileStorage {
    private Path basePath;

    public LocalFileStorage(Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public void saveFile(String path, byte[] file) throws IOException {
       Files.write(basePath.resolve(path), file);
    }

    @Override
    public byte[] getFile(String path) throws IOException {
        return Files.readAllBytes(basePath.resolve(path));
    }
}
