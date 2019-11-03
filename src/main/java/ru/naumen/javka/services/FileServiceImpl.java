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

    public List<File> getAvailableFiles(long userId){
        String ownFilesQuery = String.format("SELECT * FROM files WHERE creator=%d;", userId);
        List<File> ownFiles = this.fileRepository
                .getEntityManager()
                .createNativeQuery(ownFilesQuery)
                .getResultList();
        String accessibleViaGroupQuery = String.format("SELECT * FROM files JOIN group_files ON files.id = group_files.file_id where group_files.group_id IN (SELECT DISTINCT(group_id) from user_groups where user = %d;", userId);

        List<File> accessibleViaGroup = this.fileRepository
                .getEntityManager()
                .createNativeQuery(accessibleViaGroupQuery)
                .getResultList();

        ownFiles.addAll(accessibleViaGroup);
        return ownFiles;

    }

    public void addFile(String name, String path, String description, long creator, byte[] filedata) throws JavkaException
    {
        File file = new File(name, path, description, creator);
        fileRepository.save(file);
        try {
            storage.savePath(path, filedata);
        } catch (IOException io) {
            throw new SaveFileException(path, io);
        }
    }

    public List<File> getDirectoryContent(long directoryId){
        String query = String.format("SELECT * FROM files WHERE \"parentId\"=%d;", directoryId);
        return fileRepository
                .getEntityManager()
                .createNativeQuery(query)
                .getResultList();
    }

    public void shareWithUser(long fileId, long userId) throws JavkaException {
        String query = String.format("INSERT INTO user_files VALUES (%d, %d)", userId, fileId);
        fileRepository.getEntityManager().createNativeQuery(query).executeUpdate();
    }

    public void shareWithGroup(long fileId, long groupId) throws JavkaException {
        String query = String.format("INSERT INTO file_groups VALUES (%d, %d)", fileId, groupId);
        fileRepository.getEntityManager().createNativeQuery(query).executeUpdate();
    }
}
