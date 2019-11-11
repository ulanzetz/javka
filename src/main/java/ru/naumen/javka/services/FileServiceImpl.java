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

    private SessionManager sessionManager;

    public FileServiceImpl(FileStorage storage, FileRepository fileRepository, SessionManager sessionManager) {
        this.storage = storage;
        this.sessionManager = sessionManager;
        this.fileRepository = fileRepository;
    }

    @Override
    public byte[] getFile(String session, String path) throws JavkaException {
        Optional<Long> userId;
        try {
            userId = sessionManager.userId(session);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (!userId.isPresent())
            throw new NoPermissionException();
        try {
            return storage.getFile(path);
        } catch (IOException io) {
            throw new GetFileException(path, io);
        }
    }

    public List<File> getAvailableFiles(String session) throws NoPermissionException {
        Optional<Long> userId;
        try {
            userId = sessionManager.userId(session);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (!userId.isPresent())
            throw new NoPermissionException();
        return fileRepository.getAvailableFiles(userId.get());
    }

    public void addFile(String session, String name, String path, String description, byte[] filedata) throws JavkaException {
        Optional<Long> userId;
        try {
            userId = sessionManager.userId(session);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (!userId.isPresent())
            throw new NoPermissionException();
        File file = new File(name, path, description, userId.get());
        try {
            storage.saveFile(path, filedata);
        } catch (IOException io) {
            throw new SaveFileException(path, io);
        }
        fileRepository.save(file);
    }

    public List<File> getDirectoryContent(String session, long directoryId) throws NoPermissionException{
        Optional<Long> userId;
        try {
            userId = sessionManager.userId(session);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (!userId.isPresent())
            throw new NoPermissionException();
        return fileRepository.getDirectoryContent(userId.get(), directoryId);
    }

    public void shareWithUser(String session, long fileId, long userId) throws NoPermissionException {
        File file = fileRepository.findOne(fileId);
        Optional<Long> ownerId;
        try {
            ownerId = sessionManager.userId(session);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (!ownerId.isPresent())
            throw new NoPermissionException();
        if (!file.getCreator().equals(ownerId.get()))
            throw new NoPermissionException();
        fileRepository.shareWithUser(fileId, userId);
    }

    public void shareWithGroup(String session, long fileId, long groupId) throws NoPermissionException {
        File file = fileRepository.findOne(fileId);
        Optional<Long> ownerId;
        try {
            ownerId = sessionManager.userId(session);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (!ownerId.isPresent())
            throw new NoPermissionException();
        if (!file.getCreator().equals(ownerId.get()))
            throw new NoPermissionException();
        fileRepository.shareWithGroup(fileId, groupId);
    }
}
