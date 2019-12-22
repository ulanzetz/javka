package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.GetFileException;
import ru.naumen.javka.exceptions.JavkaException;
import ru.naumen.javka.exceptions.NoPermissionException;
import ru.naumen.javka.exceptions.SaveFileException;
import ru.naumen.javka.repositories.FileRepository;
import ru.naumen.javka.session.SessionManager;
import ru.naumen.javka.storage.FileStorage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FileServiceImpl implements FileService {
    private FileStorage storage;

    private FileRepository fileRepository;

    public FileServiceImpl(FileStorage storage, FileRepository fileRepository) {
        this.storage = storage;
        this.fileRepository = fileRepository;
    }

    public byte[] getFile(long userId, String path) throws JavkaException {
        boolean available = false;
        for (File file : getAvailableFiles(userId)){
            if (file.getPath().equals(path)){
                available = true;
                break;
            }
        }
        if (!available){
            throw new NoPermissionException();
        }
        try {
            return storage.getFile(path);
        } catch (IOException io) {
            throw new GetFileException(path, io);
        }
    }

    public List<File> getAvailableFiles(long userId) {
        return fileRepository.getAvailableFiles(userId);
    }

    public void addFile(long userId, String name, String path, String description, byte[] file) throws JavkaException {
        try {
            // FIXME: no path
            storage.saveFile(name, file);
        } catch (IOException io) {
            throw new SaveFileException(path, io);
        }
        // FIXME: don't work
        fileRepository.save(new File(name, path, description, userId));
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
}
