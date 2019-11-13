package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.Group;

import javax.persistence.EntityManager;
import java.util.List;

public class GroupRepositoryImpl extends SimpleJpaRepository<Group, Long> implements GroupRepository {
    public GroupRepositoryImpl(EntityManager em) {
        super(Group.class, em);
        this.entityManager = em;
    }

    private EntityManager entityManager;

    public List<Group> getUserGroups(long userId) {
        return entityManager
                .createNativeQuery("SELECT id, name, creator FROM groups LEFT JOIN user_groups on id = group_id WHERE user_id =?1 OR creator =?1 ORDER BY id")
                .setParameter(1, userId)
                .getResultList();

    }

    public long createGroup(long creatorId, String name) {
        return (long) (int) entityManager
                .createNativeQuery("INSERT into groups (name, creator) VALUES (?1, ?2) RETURNING id")
                .setParameter(1, name)
                .setParameter(2, creatorId)
                .getSingleResult();
    }
}

