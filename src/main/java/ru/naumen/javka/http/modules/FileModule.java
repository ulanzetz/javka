package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.http.scaladsl.model.StatusCodes;
import akka.util.ByteString;
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
                        uuidParam("fileId", fileId ->
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
                        uuidParam("directoryId", directoryId ->
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
                        uuidParam("fileId", fileId ->
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

        Route upload = extractRequestContext(ctx ->
                pathPrefix("upload", () ->
                        userId(userId ->
                                parameter("description", description ->
                                        optUuidParam("parent", parentId ->
                                                parameter("name", name ->
                                                        fileUpload("file", (fileInfo, stream) ->
                                                                onSuccess(stream.runFold(ByteString.empty(), ByteString::concat, ctx.getMaterializer()), byteString -> {
                                                                    try {
                                                                        fileService.addFile(userId, name, parentId, description, byteString.toArray());
                                                                        return ok();
                                                                    } catch (JavkaException javka) {
                                                                        return javkaError(javka);
                                                                    } catch (Throwable th) {
                                                                        return internalError(th);
                                                                    }
                                                                })
                                                        )))))
                ));

        Route download = pathPrefix("download", () ->
                userId(userId -> uuidParam("fileId", fileId -> {
                    try {
                        return binaryComplete(fileService.getFile(userId, fileId));
                    } catch (JavkaException javka) {
                        return javkaError(javka);
                    } catch (Throwable th) {
                        return internalError(th);
                    }
                })));

        Route createFolder =
                pathPrefix("createFolder", () ->
                        optUuidParam("parent", parentId ->
                                parameter("name", name ->
                                        userId(userId ->
                                        {
                                            try {
                                                fileService.createDirectory(userId, name, parentId);
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

        return pathPrefix("files", () -> concat(
                shareWithGroup,
                shareWithUser,
                getAvailableFiles,
                getDirectoryContent,
                upload,
                download,
                createFolder
                )
        );
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
