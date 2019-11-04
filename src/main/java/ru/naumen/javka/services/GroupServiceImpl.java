package ru.naumen.javka.services;

import ru.naumen.javka.domain.Group;
import ru.naumen.javka.repositories.GroupRepository;

import javax.persistence.Query;
import java.util.List;

public class GroupServiceImpl {
    private GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllAvailable(long creatorId) {
        return groupRepository.getAllAvailable(creatorId);
    }

    void create(String name, long creatorId, long[] userIds) {
        Group group = new Group(name, creatorId);
        groupRepository.save(group);
        groupRepository.addUsersToGroup(group.getId(), userIds);
    };
}
