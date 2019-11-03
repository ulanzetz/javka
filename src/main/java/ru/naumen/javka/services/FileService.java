package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.JavkaException;

public interface FileService {
    byte[] getFile(long userId, String path) throws JavkaException;
    Iterable<File> getAvailableFiles(long userId) throws JavkaException;
    Iterable<File> getDirectoryContent(long directoryId) throws JavkaException;
    void addFile(String name, String path, String description, long creator, byte[] file) throws JavkaException;
    void shareWithUser(long fileId, long userId) throws JavkaException;
    void shareWithGroup(long fileId, long groupId) throws JavkaException;
}
