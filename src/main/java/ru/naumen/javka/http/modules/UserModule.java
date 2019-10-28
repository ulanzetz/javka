package ru.naumen.javka.http.modules;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.services.UserService;

public class UserModule extends HttpModule {
    private UserService userService;
    private Logger logger;

    public UserModule(UserService userService) {
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(UserModule.class);
    }

    @Override
    public Route api() {
        return concat(pathPrefix("users", () -> complete(StatusCodes.OK, userService.list(), Jackson.marshaller())));
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
