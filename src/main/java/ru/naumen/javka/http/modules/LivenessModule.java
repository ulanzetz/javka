package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class LivenessModule extends AllDirectives implements HttpModule {
    public LivenessModule() {

    }

    @Override
    public Route route() {
        return concat(pathPrefix("liveness", () ->
                complete("OK")
        ));
    }
}
