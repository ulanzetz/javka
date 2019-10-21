package ru.naumen.javka.http.modules;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import ru.naumen.javka.services.UserService;

public class UserModule extends AllDirectives implements HttpModule {
    private UserService userService;

    public UserModule(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Route route() {
        return concat(pathPrefix("users", () -> complete(StatusCodes.OK, userService.list(), Jackson.marshaller())));
    }
}
