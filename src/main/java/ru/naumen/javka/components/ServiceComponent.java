package ru.naumen.javka.components;

import ru.naumen.javka.services.UserService;
import ru.naumen.javka.services.UserServiceImpl;

public class ServiceComponent {
    private UserService userService;

    public ServiceComponent(RepositoryComponent repos) {
        userService = new UserServiceImpl(repos.getUserRepository());
    }

    public UserService getUserService() {
        return userService;
    }
}
