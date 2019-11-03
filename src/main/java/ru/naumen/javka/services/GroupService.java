package ru.naumen.javka.services;

import ru.naumen.javka.domain.Group;
import ru.naumen.javka.exceptions.JavkaException;

public interface GroupService {
    void create(String name, long creatorId, long[] userIds) throws JavkaException;
    Iterable<Group> getAllAvailable(long creatorId) throws JavkaException;
}
