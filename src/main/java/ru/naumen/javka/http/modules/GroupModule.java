package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.services.GroupService;
import ru.naumen.javka.session.SessionManager;

import java.util.HashMap;

public class GroupModule extends SessionModule {
    private GroupService groupService;
    private Logger logger;

    public GroupModule(GroupService groupService, SessionManager sessionManager) {
        super(sessionManager);
        this.groupService = groupService;
        this.logger = LoggerFactory.getLogger(ru.naumen.javka.http.modules.GroupModule.class);
    }

    @Override
    public Route api() {
        Route create = pathPrefix("create", () ->
                parameterList("userIds", userIds ->
                parameter("name", name ->
                        userId(userId -> {
                            try {
                                Object[] userIdsArray = userIds.toArray();
                                long[] users = new long[userIdsArray.length];
                                for(int i=0; i < users.length; i++) {
                                    users[i] = Long.parseLong(userIdsArray[i].toString());
                                }
                                Long id = groupService.create(userId, name, users);
                                return jsonComplete(new HashMap<String, Long>() {
                                    {
                                        put("id", id);
                                    }
                                });
                            } catch (Throwable th) {
                                return internalError(th);
                            }
                        })
                ))
        );

        Route getAllAvailable = pathEndOrSingleSlash(() ->
                userId(userId -> {
                            try {
                                return jsonComplete(groupService.getUserGroups(userId));
                            } catch (Throwable th) {
                                return internalError(th);
                            }
                        }
                )
        );

        return pathPrefix("groups", () -> concat(create, getAllAvailable));
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
