package ru.naumen.javka.services;

import ru.naumen.javka.domain.User;
import ru.naumen.javka.repositories.UserRepository;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> getAll() {return userRepository.findAll();}
}
