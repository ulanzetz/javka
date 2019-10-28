package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LivenessModule extends HttpModule {
    private Logger logger;

    public LivenessModule() {
        this.logger = LoggerFactory.getLogger(LivenessModule.class);
    }

    @Override
    public Route api() {
        return concat(pathPrefix("liveness", () ->
                complete("OK")
        ));
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
