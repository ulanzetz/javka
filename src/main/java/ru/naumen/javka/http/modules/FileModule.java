package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import akka.http.scaladsl.model.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.exceptions.JavkaException;
import ru.naumen.javka.services.FileService;
import ru.naumen.javka.session.SessionManager;

public class FileModule extends SessionModule {
    private FileService fileService;
    private Logger logger;

    public FileModule(FileService fileService, SessionManager sessionManager) {
        super(sessionManager);
        this.fileService = fileService;
        logger = LoggerFactory.getLogger(FileModule.class);
    }

    @Override
    public Route api() {
        Route shareWithUser =
                pathPrefix("shareWithUser", () ->
                        longParam("fileId", fileId ->
                                longParam("otherUserId", otherUserId ->
                                        userId(userId ->
                                        {
                                            try {
                                                fileService.shareWithUser(userId, fileId, otherUserId);
                                                return ok();
                                            } catch (JavkaException javka) {
                                                return javkaError(javka);
                                            } catch (Throwable th) {
                                                return internalError(th);
                                            }
                                        })
                                )
                        )
                );

        Route getDirectoryContent =
                pathPrefix("getDirectoryContent", () ->
                        longParam("directoryId", directoryId ->
                                userId(userId ->
                                {
                                    try {
                                        return jsonComplete(fileService.getDirectoryContent(userId, directoryId));
                                    } catch (JavkaException javka) {
                                        return javkaError(javka);
                                    } catch (Throwable th) {
                                        return internalError(th);
                                    }
                                })
                        )
                );

        Route getAvailableFiles =
                pathPrefix("getAvailableFiles", () ->
                        userId(userId ->
                        {
                            try {
                                return jsonComplete(fileService.getAvailableFiles(userId));
                            } catch (JavkaException javka) {
                                return javkaError(javka);
                            } catch (Throwable th) {
                                return internalError(th);
                            }
                        })
                );

        Route shareWithGroup =
                pathPrefix("shareWithGroup", () ->
                        longParam("fileId", fileId ->
                                longParam("groupId", groupId ->
                                        userId(userId ->
                                        {
                                            try {
                                                fileService.shareWithGroup(userId, fileId, groupId);
                                                return complete(StatusCodes.OK());
                                            } catch (JavkaException javka) {
                                                return javkaError(javka);
                                            } catch (Throwable th) {
                                                return internalError(th);
                                            }
                                        })
                                )
                        )
                );

        return pathPrefix("files", () -> concat(shareWithGroup, shareWithUser, getAvailableFiles, getDirectoryContent));
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
