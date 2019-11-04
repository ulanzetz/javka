package ru.naumen.javka.services;

import ru.naumen.javka.domain.File;
import ru.naumen.javka.exceptions.GetFileException;
import ru.naumen.javka.exceptions.JavkaException;
import ru.naumen.javka.exceptions.SaveFileException;
import ru.naumen.javka.repositories.FileRepository;
import ru.naumen.javka.storage.FileStorage;

import java.io.IOException;
import java.util.List;

public class FileServiceImpl implements FileService {
    private FileStorage storage;

    private FileRepository fileRepository;

    public FileServiceImpl(FileStorage storage) {
        this.storage = storage;
    }

    @Override
    public byte[] getFile(long userId, String path) throws JavkaException {
        try {
            return storage.getFile(path);
        } catch (IOException io) {
            throw new GetFileException(path, io);
        }
    }

    public List<File> getAvailableFiles(long userId) {
        return fileRepository.getAvailableFiles(userId);
    }

    public void addFile(String name, String path, String description, long creator, byte[] filedata) throws JavkaException {
        File file = new File(name, path, description, creator);
        try {
            storage.saveFile(path, filedata);
        } catch (IOException io) {
            throw new SaveFileException(path, io);
        }
        fileRepository.save(file);
    }

    public List<File> getDirectoryContent(long directoryId) {
        return fileRepository.getDirectoryContent(directoryId);
    }

    public void shareWithUser(long fileId, long userId) {
        fileRepository.shareWithUser(fileId, userId);
    }

    public void shareWithGroup(long fileId, long groupId) {
        fileRepository.shareWithGroup(fileId, groupId);
    }
}
