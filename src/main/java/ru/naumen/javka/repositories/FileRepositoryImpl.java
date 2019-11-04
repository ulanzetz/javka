package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.File;

import javax.persistence.EntityManager;
import java.util.List;

public class FileRepositoryImpl extends SimpleJpaRepository<File, Long> implements FileRepository {
    public FileRepositoryImpl(EntityManager em) {
        super(File.class, em);
        this.entityManager = em;
    }

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void shareWithUser(long fileId, long userId) {
        String query = "INSERT INTO user_files VALUES (?1, ?2);";
        entityManager
                .createNativeQuery(query)
                .setParameter(1, userId)
                .setParameter(2, fileId)
                .executeUpdate();
    }

    public void shareWithGroup(long fileId, long groupId) {
        String query = "INSERT INTO file_groups VALUES (?1, ?2);";
        entityManager
                .createNativeQuery(query)
                .setParameter(1, fileId)
                .setParameter(2, groupId)
                .executeUpdate();
    }

    public List<File> getDirectoryContent(long directoryId) {
        String query = "SELECT * FROM files WHERE \"parentId\"= ?1;";
        return entityManager
                .createNativeQuery(query)
                .setParameter(1, directoryId)
                .getResultList();
    }

    public List<File> getAvailableFiles(long userId) {
        String query = "SELECT * FROM files WHERE creator=?1 " +
                "UNION " +
                "SELECT * FROM files JOIN group_files ON files.id = group_files.file_id where group_files.group_id IN (SELECT DISTINCT(group_id) from user_groups where user_id = ?1;";
        return entityManager
                .createNativeQuery(query)
                .setParameter(1, userId)
                .getResultList();
    }
}

