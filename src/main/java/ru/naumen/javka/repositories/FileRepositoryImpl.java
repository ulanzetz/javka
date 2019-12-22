package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.File;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class FileRepositoryImpl extends SimpleJpaRepository<File, Long> implements FileRepository {
    public FileRepositoryImpl(EntityManager em) {
        super(File.class, em);
        this.entityManager = em;
    }

    private EntityManager entityManager;

    public void shareWithUser(long fileId, long userId) {
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        String query = "INSERT INTO user_files VALUES (?1, ?2);";
        entityManager
                .createNativeQuery(query)
                .setParameter(1, userId)
                .setParameter(2, fileId)
                .executeUpdate();
        transaction.commit();
    }

    public void shareWithGroup(long fileId, long groupId) {
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        String query = "INSERT INTO group_files VALUES (?1, ?2);";
        entityManager
                .createNativeQuery(query)
                .setParameter(1, groupId)
                .setParameter(2, fileId)
                .executeUpdate();
        transaction.commit();
    }

    public List<File> getDirectoryContent(long userId, long directoryId) {
        String query = "SELECT *\n" +
                "FROM files\n" +
                "WHERE files.parentid = ?2 AND (" +
                "\tEXISTS (SELECT * FROM user_files WHERE user_files.user_id = ?1 AND user_files.file_id = files.id)\n" +
                "OR\n" +
                "\tEXISTS (SELECT * FROM user_groups JOIN group_files on user_groups.group_id = group_files.group_id WHERE user_groups.user_id = ?1 AND group_files.file_id = files.id)\n" +
                "OR\n" +
                "\tfiles.creator = ?1)";

        return entityManager
                .createNativeQuery(query)
                .setParameter(1, userId)
                .setParameter(2, (int) directoryId)
                .getResultList();
    }

    public List<File> getAvailableFiles(long userId) {
        String query = "SELECT *\n" +
                "FROM files\n" +
                "WHERE files.parentid = -1 AND (" +
                "\tEXISTS (SELECT * FROM user_files WHERE user_files.user_id = ?1 AND user_files.file_id = files.id)\n" +
                "OR\n" +
                "\tEXISTS (SELECT * FROM user_groups JOIN group_files on user_groups.group_id = group_files.group_id WHERE user_groups.user_id = ?1 AND group_files.file_id = files.id)\n" +
                "OR\n" +
                "\tfiles.creator = ?1)";

        return entityManager
                .createNativeQuery(query, File.class)
                .setParameter(1, userId)
                .getResultList();
    }

    public File save(File file){
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        entityManager.persist(file);
        entityManager.refresh(file);
        transaction.commit();
        return file;
    }
}

