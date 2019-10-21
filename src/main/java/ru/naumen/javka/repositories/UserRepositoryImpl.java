package ru.naumen.javka.repositories;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.naumen.javka.domain.User;

import javax.persistence.EntityManager;

public class UserRepositoryImpl extends SimpleJpaRepository<User, Long> implements UserRepository {
    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }
}
