package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.File;

import javax.persistence.EntityManager;

public class FileRepositoryImpl extends SimpleJpaRepository<File, Long> implements FileRepository {
    public FileRepositoryImpl(EntityManager em) {
        super(File.class, em);
        this.entityManager = em;
    }

    private EntityManager entityManager;

    public EntityManager getEntityManager() {return entityManager;}
}

