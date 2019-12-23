package ru.naumen.javka.services;

import ru.naumen.javka.domain.Group;
import ru.naumen.javka.repositories.GroupRepository;
import ru.naumen.javka.session.SessionManager;

import java.util.List;

public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Long create(long userId, String name, long[] userIds) {
        return groupRepository.createGroup(userId, name, userIds);
    }

    public List<Group> getUserGroups(long userId) {
        return groupRepository.getUserGroups(userId);
    }
}
