package ru.naumen.javka.http.modules;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.http.scaladsl.model.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.exceptions.JavkaException;
import ru.naumen.javka.services.FileService;

import static akka.http.javadsl.server.PathMatchers.*;

public class FileModule extends HttpModule {
    private FileService fileService;
    private Logger logger;

    public FileModule(FileService fileService) {
        this.fileService = fileService;
        logger = LoggerFactory.getLogger(FileModule.class);
    }

    @Override
    public Route api() {
        Route shareWithUser = pathPrefix("files", () ->
                pathPrefix("shareWithUser", () ->
                        parameter("fileId" ,fileId ->
                                parameter("userId" ,userId ->
                                        parameter("session", session ->
                                        {
                                            try {
                                                fileService.shareWithUser(session, Long.parseLong(fileId), Long.parseLong(userId));
                                                return complete(StatusCodes.OK());
                                            } catch (Throwable th) {
                                                return internalError(th);
                                            }
                                        })
                                )
                        ))
        );


        Route shareWithGroup = pathPrefix("files", () ->
                pathPrefix("shareWithGroup", () ->
                        parameter("fileId" ,fileId ->
                                parameter("groupId", groupId ->
                                        parameter("session", session ->
                                        {
                                            try {
                                                fileService.shareWithGroup(session, Long.parseLong(fileId), Long.parseLong(groupId));
                                                return complete(StatusCodes.OK());
                                            } catch (Throwable th) {
                                                return internalError(th);
                                            }
                                        })
                                )
                        ))
        );

        return concat(shareWithGroup, shareWithUser);
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
