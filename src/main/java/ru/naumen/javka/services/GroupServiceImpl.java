package ru.naumen.javka.services;

import ru.naumen.javka.domain.Group;
import ru.naumen.javka.exceptions.JavkaException;
import ru.naumen.javka.exceptions.NoPermissionException;
import ru.naumen.javka.repositories.GroupRepository;
import ru.naumen.javka.session.SessionManager;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class GroupServiceImpl implements GroupService{
    private GroupRepository groupRepository;
    private SessionManager sessionManager;


    public GroupServiceImpl(GroupRepository groupRepository, SessionManager sessionManager) {
        this.groupRepository = groupRepository;
        this.sessionManager = sessionManager;
    }

    public List<Group> getAllAvailable(String sessionToken) throws JavkaException{
        Optional<Long> userId;
        try {
            userId = sessionManager.userId(sessionToken);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (userId.isPresent())
            return groupRepository.getAllAvailable(userId.get());
        else throw new NoPermissionException();
    }

    public void create(String name, String sessionToken, long[] userIds) throws JavkaException{
        Optional<Long> userId;
        try {
            userId = sessionManager.userId(sessionToken);
        } catch (Throwable th){
            throw new NoPermissionException();
        }
        if (!userId.isPresent())
            throw new NoPermissionException();

        Group group = new Group(name, userId.get());
        groupRepository.getEntityManager().getTransaction().begin();
        group = groupRepository.save(group);
        groupRepository.getEntityManager().getTransaction().commit();
        groupRepository.addUsersToGroup(group.getId(), userIds);

    };
}
