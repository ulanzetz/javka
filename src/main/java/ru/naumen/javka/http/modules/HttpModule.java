package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;

public interface HttpModule {
     Route route();
}
