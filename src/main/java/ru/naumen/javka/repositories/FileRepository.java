package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.File;

import java.util.List;

public interface FileRepository extends CrudRepository<File, Long> {
    void shareWithUser(long fileId, long userId);

    void shareWithGroup(long fileId, long groupId);

    List<File> getDirectoryContent(long userId, long directoryId);

    List<File> getAvailableFiles(long userId);
}
