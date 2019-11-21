package ru.naumen.javka.http;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.directives.RouteDirectives;
import akka.stream.ActorMaterializer;
import ru.naumen.javka.http.modules.HttpModule;
import scala.collection.immutable.List;

public class Server extends RouteDirectives {
    private Route route;

    private Route staticContent() {
        return concat(
                pathEndOrSingleSlash(() -> getFromResource("static/index.html")),
                getFromResourceDirectory("static")
        );
    }

    private Route api(List<HttpModule> modules) {
        if (modules.size() == 0)
            throw new RuntimeException("Can't create server without modules");

        return pathPrefix("api", () -> concat(modules.head().api(), modules.mapConserve(HttpModule::api)));
    }

    public Server(List<HttpModule> modules) {
        route = concat(api(modules), staticContent());
    }

    public void start(String host, int port, ActorSystem system, ActorMaterializer materializer) {
        Http.get(system).bindAndHandle(route.flow(system, materializer), ConnectHttp.toHost(host, port), materializer);
    }
}
