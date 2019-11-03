package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.File;

import javax.persistence.EntityManager;

public interface FileRepository extends CrudRepository<File, Long> {
    EntityManager getEntityManager();
}
