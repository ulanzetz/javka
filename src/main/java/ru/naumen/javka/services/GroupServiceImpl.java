package ru.naumen.javka.services;

import ru.naumen.javka.domain.Group;
import ru.naumen.javka.repositories.GroupRepository;

import java.util.List;

public class GroupServiceImpl {
    private GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllAvailable(long creatorId) {
        // Generaly it is not secure, but we are using typed template here which should not allow
        // an insertion of string
        String query = String.format("SELECT * FROM groups WHERE creator=%d;", creatorId);
        return groupRepository
                .getEntityManager()
                .createNativeQuery(query)
                .getResultList();
    }

    void create(String name, long creatorId, long[] userIds) {
        Group group = new Group(name, creatorId);
        groupRepository.save(group);
        for (long userId : userIds) {
            String query = String.format("INSERT INTO user_groups VALUES (%d, %d)", userId, group.getId());
            groupRepository.getEntityManager().createNativeQuery(query).executeUpdate();
        }
    }

    ;

}
