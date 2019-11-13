package ru.naumen.javka.services;

import ru.naumen.javka.domain.User;
import ru.naumen.javka.repositories.UserRepository;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(long id) {
        User user = userRepository.findOne(id);
        user.cleanPasswordHash();
        return user;
    }

    public Iterable<User> getAll() {
        Iterable<User> iter = userRepository.findAll();
        iter.forEach(User::cleanPasswordHash);
        return iter;
    }
}
