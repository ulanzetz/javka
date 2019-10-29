package ru.naumen.javka.services;

import java.util.Optional;

public interface SignUpService {
    Optional<String> session(Long userId, String password) throws Throwable;
}
