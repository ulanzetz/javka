package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.GetFileException;
import ru.naumen.javka.exceptions.JavkaException;
import ru.naumen.javka.exceptions.NoPermissionException;
import ru.naumen.javka.exceptions.SaveFileException;
import ru.naumen.javka.repositories.FileRepository;
import ru.naumen.javka.storage.FileStorage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileServiceImpl implements FileService {
    private FileStorage storage;

    private FileRepository fileRepository;

    public FileServiceImpl(FileStorage storage, FileRepository fileRepository) {
        this.storage = storage;
        this.fileRepository = fileRepository;
    }

    public byte[] getFile(long userId, UUID fileId) throws JavkaException {
        if (!fileRepository.isFileAccessible(userId, fileId.toString())) {
            throw new NoPermissionException();
        }
        File file = fileRepository.findOne(fileId.toString());
        try {
            return storage.getFile(file.getId());
        } catch (IOException io) {
            throw new GetFileException(file.getId(), io);
        }
    }

    public List<File> getAvailableFiles(long userId) {
        return fileRepository.getAvailableFiles(userId);
    }

    public void addFile(long userId, String name, Optional<UUID> parentId, String description, byte[] file) throws JavkaException {
        File fileMeta = new File(java.util.UUID.randomUUID().toString(), name, userId, parentId.map(UUID::toString), description, false);
        try {
            storage.saveFile(fileMeta.getId(), file);
        } catch (IOException io) {
            throw new SaveFileException(fileMeta.getId(), io);
        }
        fileRepository.save(fileMeta);
    }

    public List<File> getDirectoryContent(long userId, UUID directoryId) {
        return fileRepository.getDirectoryContent(userId, directoryId.toString());
    }

    public void shareWithUser(long userId, UUID fileId, long otherUserId) throws JavkaException {
        File file = fileRepository.findOne(fileId.toString());
        if (!file.getCreator().equals(userId))
            throw new NoPermissionException();
        fileRepository.shareWithUser(fileId.toString(), otherUserId);
    }

    public void shareWithGroup(long userId, UUID fileId, long groupId) throws NoPermissionException {
        File file = fileRepository.findOne(fileId.toString());
        if (!file.getCreator().equals(userId))
            throw new NoPermissionException();
        fileRepository.shareWithGroup(fileId.toString(), groupId);
    }

    public void createDirectory(long userId, String name, Optional<UUID> parentId) {
        File file = new File(java.util.UUID.randomUUID().toString(), name, userId, parentId.map(UUID::toString), null, true);
        fileRepository.save(file);
    }
}
