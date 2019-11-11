package ru.naumen.javka.components;

import ru.naumen.javka.repositories.*;

import javax.persistence.EntityManager;

public class RepositoryComponent {
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private FileRepository fileRepository;

    public RepositoryComponent(DbComponent db) {
        EntityManager em = db.getEm();
        userRepository = new UserRepositoryImpl(em);
        groupRepository = new GroupRepositoryImpl(em);
        fileRepository = new FileRepositoryImpl(em);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public GroupRepository getGroupRepository() {
        return groupRepository;
    }

    public FileRepository getFileRepository() {
        return fileRepository;
    }
}
