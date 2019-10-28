package ru.naumen.javka;

import com.typesafe.config.Config;
import ru.naumen.javka.components.*;
import ru.naumen.javka.http.Server;
import ru.naumen.javka.storage.FileStorage;
import ru.naumen.javka.storage.LocalFileStorage;
import scala.collection.JavaConverters;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        ExecutionComponent exec = new ExecutionComponent();
        Config cfg = exec.getConfig();

        DbComponent db = new DbComponent(cfg.getString("db.host"), cfg.getString("db.user"), cfg.getString("db.password"));
        RepositoryComponent repos = new RepositoryComponent(db);
        FileStorage fileStorage = new LocalFileStorage(Paths.get(cfg.getString("storage.path")));
        ServiceComponent services = new ServiceComponent(repos, fileStorage);
        ModuleComponent modules = new ModuleComponent(services);

        String host = cfg.getString("server.host");
        int port = cfg.getInt("server.port");

        new Server(JavaConverters.asScalaBuffer(modules.getList()).toList())
                .start(host, port, exec.getSystem(), exec.getMaterializer());
        System.out.println("Server started on " + port);
    }
}
