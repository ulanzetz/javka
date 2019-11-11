package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.Group;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class GroupRepositoryImpl extends SimpleJpaRepository<Group, Long> implements GroupRepository {
    public GroupRepositoryImpl(EntityManager em) {
        super(Group.class, em);
        this.entityManager = em;
    }

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void addUsersToGroup(long groupId, long[] userIds) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO user_groups VALUES ");
        for (int i = 0; i< userIds.length; i++) {
            stringBuilder.append(String.format("(?%d, ?0),", i+1));
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append(";");
        Query nativeQuery = entityManager.createNativeQuery(stringBuilder.toString());
        nativeQuery.setParameter(0, groupId);
        for (int i = 0; i< userIds.length; i++) {
            nativeQuery.setParameter(i+1, userIds[i]);
        }
        entityManager.getTransaction().begin();
        nativeQuery.executeUpdate();
        entityManager.getTransaction().commit();
    }

    public List<Group> getAllAvailable(long creatorId) {
        String query = "SELECT * FROM groups WHERE creator=?1";
        return entityManager
                .createNativeQuery(query)
                .setParameter(1, creatorId)
                .getResultList();

    }
}

