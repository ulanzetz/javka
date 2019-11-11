package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByName(String name);
    Long save(String name, String passwordHash);
}
