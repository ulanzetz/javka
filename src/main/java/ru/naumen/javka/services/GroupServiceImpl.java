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
        String query = "SELECT * FROM groups WHERE creator=?1;";
        return groupRepository
                .getEntityManager()
                .createNativeQuery(query)
                .setParameter(1, creatorId)
                .getResultList();
    }

    void create(String name, long creatorId, long[] userIds) {
        Group group = new Group(name, creatorId);
        groupRepository.save(group);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO user_groups VALUES ");
        for (int i = 0; i< userIds.length; i++) {
            stringBuilder.append(String.format("(%d, ?0) ", i+1));
        }
        stringBuilder.append(";");
        Query nativeQuery = groupRepository.getEntityManager().createNativeQuery(stringBuilder.toString());
        nativeQuery.setParameter(0, group.getId());
        for (int i = 0; i< userIds.length; i++) {
            nativeQuery.setParameter(i+1, userIds[i]);
        }
        nativeQuery.executeUpdate();
    }

    ;

}
