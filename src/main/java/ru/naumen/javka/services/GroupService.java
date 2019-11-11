package ru.naumen.javka.services;

import ru.naumen.javka.domain.Group;
import ru.naumen.javka.exceptions.JavkaException;

import java.util.List;

public interface GroupService {
    void create(String name, String sessionToken, long[] userIds) throws JavkaException;

    Iterable<Group> getAllAvailable(String sessionToken) throws JavkaException;

}
