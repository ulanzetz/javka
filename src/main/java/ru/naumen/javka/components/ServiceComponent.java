package ru.naumen.javka.components;

import ru.naumen.javka.services.FileService;
import ru.naumen.javka.services.FileServiceImpl;
import ru.naumen.javka.services.UserService;
import ru.naumen.javka.services.UserServiceImpl;
import ru.naumen.javka.storage.FileStorage;

public class ServiceComponent {
    private UserService userService;
    private FileService fileService;

    public ServiceComponent(RepositoryComponent repos, FileStorage fileStorage) {
        userService = new UserServiceImpl(repos.getUserRepository());
        fileService = new FileServiceImpl(fileStorage);
    }

    public UserService getUserService() {
        return userService;
    }

    public FileService getFileService() {
        return fileService;
    }
}
