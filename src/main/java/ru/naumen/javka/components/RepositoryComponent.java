package ru.naumen.javka.components;

import ru.naumen.javka.repositories.GroupRepository;
import ru.naumen.javka.repositories.GroupRepositoryImpl;
import ru.naumen.javka.repositories.UserRepository;
import ru.naumen.javka.repositories.UserRepositoryImpl;

import javax.persistence.EntityManager;

public class RepositoryComponent {
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    public RepositoryComponent(DbComponent db) {
        EntityManager em = db.getEm();
        userRepository = new UserRepositoryImpl(em);
        groupRepository = new GroupRepositoryImpl(em);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public GroupRepository getGroupRepository() {
        return groupRepository;
    }
}
