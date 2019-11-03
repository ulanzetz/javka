package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.Group;

import javax.persistence.EntityManager;

public interface GroupRepository extends CrudRepository<Group, Long> {
    EntityManager getEntityManager();
}
