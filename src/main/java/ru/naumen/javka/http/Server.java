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

    public Server(List<HttpModule> modules) {
        if (modules.size() == 0)
            throw new RuntimeException("Can't create server without modules");
        route = concat(modules.head().route(), modules.mapConserve(HttpModule::route));
    }

    public void start(String host, int port, ActorSystem system, ActorMaterializer materializer) {
        Http.get(system).bindAndHandle(route.flow(system, materializer), ConnectHttp.toHost(host, port), materializer);
    }
}
