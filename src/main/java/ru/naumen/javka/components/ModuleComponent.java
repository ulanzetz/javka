package ru.naumen.javka.components;

import ru.naumen.javka.http.modules.FileModule;
import ru.naumen.javka.http.modules.HttpModule;
import ru.naumen.javka.http.modules.LivenessModule;
import ru.naumen.javka.http.modules.UserModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleComponent {
    private List<HttpModule> list;

    public ModuleComponent(ServiceComponent services) {
        list = new ArrayList<>();
        list.add(new LivenessModule());
        list.add(new UserModule(services.getUserService()));
        list.add(new FileModule(services.getFileService()));
    }

    public List<HttpModule> getList() {
        return list;
    }
}
