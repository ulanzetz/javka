package ru.naumen.javka.services;

import ru.naumen.javka.domain.User;
import ru.naumen.javka.repositories.UserRepository;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> list() {
        return userRepository.findAll();
    }
}
