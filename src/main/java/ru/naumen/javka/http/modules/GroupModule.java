package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Complete;
import akka.http.javadsl.server.Route;
import akka.http.scaladsl.model.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.services.GroupService;
import ru.naumen.javka.session.SessionManager;

import java.util.ArrayList;

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
        Route create = pathPrefix("groups", () ->
                pathPrefix("create", () ->
                    parameter("name" ,name ->
                      parameter("session", session ->
                          parameterList("userIds", userIds -> {
                              try {
                                  Object[] userIdsArray = userIds.toArray();
                                  long[] users = new long[userIdsArray.length];
                                  for(int i=0; i < users.length; i++) {
                                      users[i] = Long.parseLong(userIdsArray[i].toString());
                                  }
                                   groupService.create(name, session, users);
                                   return complete(StatusCodes.OK());
                              } catch (Throwable th) {
                                  return internalError(th);
                              }
                          })
                      )
                    ))
                );


        Route getAllAvailable = pathPrefix("groups", () ->
                parameter("session", session -> {
                            try {
                                return jsonComplete(groupService.getAllAvailable(session));
                            } catch (Throwable th) {
                                return internalError(th);
                            }
                        }
                )
        );

        return concat(create, getAllAvailable);
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
