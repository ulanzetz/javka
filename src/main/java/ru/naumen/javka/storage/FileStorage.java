package ru.naumen.javka.storage;

import java.io.IOException;

public interface FileStorage {
    byte[] getFile(String path) throws IOException;
}
