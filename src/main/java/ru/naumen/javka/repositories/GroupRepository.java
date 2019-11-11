package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.Group;

import javax.persistence.EntityManager;
import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {
    EntityManager getEntityManager();
    void addUsersToGroup(long groupId, long[] userIds);
    List<Group> getAllAvailable(long creatorId);
}
