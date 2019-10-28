package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import ru.naumen.javka.exceptions.NoPermissionException;
import ru.naumen.javka.session.SessionManager;

import java.util.Optional;
import java.util.function.Function;

abstract class SessionModule extends HttpModule {
    private SessionManager sessionManager;

    SessionModule(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    Route userId(Function<Long, Route> inner) {
        return parameter("session", session -> {
            try {
                Optional<Long> userId = sessionManager.userId(session);
                if (userId.isPresent())
                    return inner.apply(userId.get());
                else return javkaError(new NoPermissionException());
            } catch (Throwable th) {
                return internalError(th);
            }
        });
    }
}
