package ru.naumen.javka.session;

import java.util.Optional;

public interface SessionManager {
    Optional<Long> userId(String session) throws Throwable;

    String session(Long userId) throws Throwable;
}
