package ru.naumen.javka.services;

import ru.naumen.javka.domain.User;

public interface UserService {
    User get(long id);
    Iterable<User> getAll();
}
