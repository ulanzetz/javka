package ru.naumen.javka.services;

import ru.naumen.javka.domain.User;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface SignUpService {
    Optional<String> session(Long userId, String password) throws Throwable;
    User create(String name, String password) throws NoSuchAlgorithmException;
}
