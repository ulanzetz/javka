package ru.naumen.javka.components;

import ru.naumen.javka.services.*;
import ru.naumen.javka.session.SessionManager;
import ru.naumen.javka.storage.FileStorage;

public class ServiceComponent {
    private UserService userService;
    private FileService fileService;
    private SignUpService signUpService;
    private GroupService groupService;

    public ServiceComponent(RepositoryComponent repos,
                            FileStorage fileStorage,
                            SessionManager sessionManager,
                            String passwordSalt) {
        userService = new UserServiceImpl(repos.getUserRepository());
        fileService = new FileServiceImpl(fileStorage, repos.getFileRepository(), sessionManager);
        signUpService = new SignUpSeviceImpl(sessionManager, repos.getUserRepository(), passwordSalt);
        groupService = new GroupServiceImpl(repos.getGroupRepository(), sessionManager);
    }

    public UserService getUserService() {
        return userService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public SignUpService getSignUpService() {
        return signUpService;
    }

    public GroupService getGroupService() {
        return groupService;
    }
}
