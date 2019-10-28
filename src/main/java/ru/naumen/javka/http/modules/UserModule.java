package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.services.UserService;
import ru.naumen.javka.session.SessionManager;

public class UserModule extends SessionModule {
    private UserService userService;
    private Logger logger;

    public UserModule(UserService userService, SessionManager sessionManager) {
        super(sessionManager);
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(UserModule.class);
    }

    @Override
    public Route api() {
        return pathPrefix("users", () ->
                pathPrefix("current", () -> userId(userId -> jsonComplete(userService.get(userId))))
        );
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
