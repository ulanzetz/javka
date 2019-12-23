package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.File;

import java.util.List;

public interface FileRepository extends CrudRepository<File, String> {
    void shareWithUser(String fileId, long userId);

    void shareWithGroup(String fileId, long groupId);

    List<File> getDirectoryContent(long userId, String directoryId);

    List<File> getAvailableFiles(long userId);

    boolean isFileAccessible(long userId, String fileId);
}
