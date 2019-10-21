package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
