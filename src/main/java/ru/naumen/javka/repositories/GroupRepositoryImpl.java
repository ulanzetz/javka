package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.Group;

import javax.persistence.EntityManager;

public class GroupRepositoryImpl extends SimpleJpaRepository<Group, Long> implements GroupRepository {
    public GroupRepositoryImpl(EntityManager em) {
        super(Group.class, em);
        this.entityManager = em;
    }

    private EntityManager entityManager;

    public EntityManager getEntityManager() {return entityManager;}
}

