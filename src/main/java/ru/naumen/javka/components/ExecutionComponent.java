package ru.naumen.javka.components;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ExecutionComponent {
    private Config config;
    private ActorSystem system;
    private ActorMaterializer materializer;

    public ExecutionComponent() {
        config = ConfigFactory.load("application.conf");
        system = ActorSystem.create("javka-akka", config);
        materializer = ActorMaterializer.create(system);
    }

    public ActorMaterializer getMaterializer() {
        return materializer;
    }

    public ActorSystem getSystem() {
        return system;
    }

    public Config getConfig() {
        return config;
    }
}
