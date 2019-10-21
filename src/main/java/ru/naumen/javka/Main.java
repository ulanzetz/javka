package ru.naumen.javka;

import com.typesafe.config.Config;
import ru.naumen.javka.components.*;
import ru.naumen.javka.http.Server;
import scala.collection.JavaConverters;

public class Main {
    public static void main(String[] args) {
        ExecutionComponent exec = new ExecutionComponent();
        Config cfg = exec.getConfig();

        DbComponent db = new DbComponent(cfg.getString("db.host"), cfg.getString("db.user"), cfg.getString("db.password"));
        RepositoryComponent repos = new RepositoryComponent(db);
        ServiceComponent services = new ServiceComponent(repos);
        ModuleComponent modules = new ModuleComponent(services);

        String host = cfg.getString("server.host");
        int port = cfg.getInt("server.port");

        new Server(JavaConverters.asScalaBuffer(modules.getList()).toList())
                .start(host, port, exec.getSystem(), exec.getMaterializer());
        System.out.println("Server started on " + port);
    }
}
