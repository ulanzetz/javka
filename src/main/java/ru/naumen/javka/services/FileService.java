package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.JavkaException;

public interface FileService {
    byte[] getFile(long userId, String path) throws JavkaException;
    File[] getAvailableFiles(long userId) throws JavkaException;
    File[] getDirectoryContent(long directoryId) throws JavkaException;
    void addFile(long userId, String path, byte[] file) throws JavkaException;
    void shareWithUser(long userId) throws JavkaException;
    void shareWithGroup(long groupId) throws JavkaException;
}
