package ru.naumen.javka.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javka.domain.Group;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {
    List<Group> getUserGroups(long userId);
    long createGroup(long creatorId, String name);
}
