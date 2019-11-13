package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl extends SimpleJpaRepository<User, Long> implements UserRepository {
    private EntityManager entityManager;

    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
        this.entityManager = em;
    }

    public Long save(String name, String passwordHash) {
        return (long) (int) entityManager
                .createNativeQuery("INSERT INTO users (name, password_hash) VALUES (?1, ?2) ON CONFLICT (name) DO NOTHING RETURNING id")
                .setParameter(1, name)
                .setParameter(2, passwordHash)
                .getSingleResult();
    }

    public Optional<User> findByName(String name) {
        List list = entityManager
                .createNativeQuery("SELECT * from users where name = ?1", User.class)
                .setParameter(1, name)
                .getResultList();

        if (list.size() == 0)
            return Optional.empty();
        else
            return Optional.of((User) list.get(0));
    }
}
