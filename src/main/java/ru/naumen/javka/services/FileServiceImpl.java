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

public class FileServiceImpl implements FileService {
    private FileStorage storage;

    private FileRepository fileRepository;

    public FileServiceImpl(FileStorage storage, FileRepository fileRepository) {
        this.storage = storage;
        this.fileRepository = fileRepository;
    }

    public byte[] getFile(long userId, long fileId) throws JavkaException {
        if (!fileRepository.isFileAccessible(userId, fileId)){
            throw new NoPermissionException();
        }
        File file = fileRepository.findOne(fileId);
        try {
            return storage.getFile(file.getPath());
        } catch (IOException io) {
            throw new GetFileException(file.getPath(), io);
        }
    }

    public List<File> getAvailableFiles(long userId) {
        return fileRepository.getAvailableFiles(userId);
    }

    public void addFile(long userId, String name, long parentId, String description, byte[] file) throws JavkaException {
        File fileMeta = new File(name, userId, parentId, description, false);
        try {
            storage.saveFile(fileMeta.getPath(), file);
        } catch (IOException io) {
            throw new SaveFileException(fileMeta.getPath(), io);
        }
        fileRepository.save(fileMeta);
    }

    public List<File> getDirectoryContent(long userId, long directoryId) {
        return fileRepository.getDirectoryContent(userId, directoryId);
    }

    public void shareWithUser(long userId, long fileId, long otherUserId) throws JavkaException {
        File file = fileRepository.findOne(fileId);
        if (!file.getCreator().equals(userId))
            throw new NoPermissionException();
        fileRepository.shareWithUser(fileId, userId);
    }

    public void shareWithGroup(long userId, long fileId, long groupId) throws NoPermissionException {
        File file = fileRepository.findOne(fileId);
        if (!file.getCreator().equals(userId))
            throw new NoPermissionException();
        fileRepository.shareWithGroup(fileId, groupId);
    }

    public void createDirectory(long userId, String name, long parentId) throws JavkaException {
        File file = new File(name, userId, parentId, "", true);
        fileRepository.save(file);
    }
}
