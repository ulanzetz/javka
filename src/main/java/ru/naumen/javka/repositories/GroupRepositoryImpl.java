package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.Group;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class GroupRepositoryImpl extends SimpleJpaRepository<Group, Long> implements GroupRepository {
    public GroupRepositoryImpl(EntityManager em) {
        super(Group.class, em);
        this.entityManager = em;
    }

    private EntityManager entityManager;

    public List<Group> getUserGroups(long userId) {
        return entityManager
                .createNativeQuery("SELECT id, name, creator FROM groups LEFT JOIN user_groups on id = group_id WHERE user_id =?1 OR creator =?1 ORDER BY id", Group.class)
                .setParameter(1, userId)
                .getResultList();

    }

    public long createGroup(long creatorId, String name, long[] userIds) {
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        long groupId =  (long) (int) entityManager
                .createNativeQuery("INSERT into groups (name, creator) VALUES (?1, ?2) RETURNING id")
                .setParameter(1, name)
                .setParameter(2, creatorId)
                .getSingleResult();

        addUsersToGroup(groupId, userIds);
        transaction.commit();
        return groupId;
    }

    private void addUsersToGroup(long groupId, long[] userIds) {
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
        nativeQuery.executeUpdate();
    }
}

