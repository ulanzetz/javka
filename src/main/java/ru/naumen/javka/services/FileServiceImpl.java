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
        String query = "SELECT * FROM files WHERE creator=?1 " +
                "UNION " +
                "SELECT * FROM files JOIN group_files ON files.id = group_files.file_id where group_files.group_id IN (SELECT DISTINCT(group_id) from user_groups where user_id = ?1;";
        return this.fileRepository
                .getEntityManager()
                .createNativeQuery(query)
                .setParameter(1, userId)
                .getResultList();
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
        String query = "SELECT * FROM files WHERE \"parentId\"= ?1;";
        return fileRepository
                .getEntityManager()
                .createNativeQuery(query)
                .setParameter(1, directoryId)
                .getResultList();
    }

    public void shareWithUser(long fileId, long userId) throws JavkaException {
        String query = "INSERT INTO user_files VALUES (?1, ?2);";
        fileRepository
                .getEntityManager()
                .createNativeQuery(query)
                .setParameter(1, userId)
                .setParameter(2, fileId)
                .executeUpdate();
    }

    public void shareWithGroup(long fileId, long groupId) throws JavkaException {
        String query = "INSERT INTO file_groups VALUES (?1, ?2);";
        fileRepository
                .getEntityManager()
                .createNativeQuery(query)
                .setParameter(1, fileId)
                .setParameter(2, groupId)
                .executeUpdate();
    }
}
