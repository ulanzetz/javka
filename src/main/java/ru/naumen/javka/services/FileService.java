package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.JavkaException;

public interface FileService {
    byte[] getFile(long userId, long fileId) throws JavkaException;

    Iterable<File> getAvailableFiles(long userId) throws JavkaException;

    Iterable<File> getDirectoryContent(long userId, long directoryId) throws JavkaException;

    void addFile(long userId, String name, String description, byte[] file) throws JavkaException;

    void shareWithUser(long userId, long fileId, long otherUserId) throws JavkaException;

    void shareWithGroup(long userId, long fileId, long groupId) throws JavkaException;
}
