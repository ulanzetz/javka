package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.JavkaException;

import java.util.Optional;
import java.util.UUID;

public interface FileService {
    byte[] getFile(long userId, UUID fileId) throws JavkaException;

    Iterable<File> getAvailableFiles(long userId) throws JavkaException;

    Iterable<File> getDirectoryContent(long userId, UUID directoryId) throws JavkaException;

    void createDirectory(long userId, String name, Optional<UUID> parentId) throws JavkaException;

    void addFile(long userId, String name, Optional<UUID> parentId, String description, byte[] file) throws JavkaException;

    void shareWithUser(long userId, UUID fileId, long otherUserId) throws JavkaException;

    void shareWithGroup(long userId, UUID fileId, long groupId) throws JavkaException;
}
