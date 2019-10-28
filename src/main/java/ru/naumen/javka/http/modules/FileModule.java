package ru.naumen.javka.http.modules;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
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
        return pathPrefix(segment("files").slash(segment()), fileName -> {
            try {
                return complete(HttpResponse.create().withEntity(fileService.getFile(1, fileName)));
            } catch (JavkaException javka) {
                return javkaError(javka);
            } catch (Throwable th) {
                return internalError(th);
            }
        });
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
