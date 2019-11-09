package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.JavkaException;

public interface FileService {
    byte[] getFile(String session, String path) throws JavkaException;

    Iterable<File> getAvailableFiles(String session) throws JavkaException;

    Iterable<File> getDirectoryContent(String session, long directoryId) throws JavkaException;

    void addFile(String session, String name, String path, String description, byte[] file) throws JavkaException;

    void shareWithUser(String session, long fileId, long userId) throws JavkaException;

    void shareWithGroup(String session, long fileId, long groupId) throws JavkaException;
}
