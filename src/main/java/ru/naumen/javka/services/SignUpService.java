package ru.naumen.javka.services;

import java.util.Optional;

public interface SignUpService {
    Optional<String> signUp(String userName, String password) throws Throwable;
}
