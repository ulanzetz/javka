package ru.naumen.javka.components;

import ru.naumen.javka.http.modules.*;
import ru.naumen.javka.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ModuleComponent {
    private List<HttpModule> list;

    public ModuleComponent(ServiceComponent services, SessionManager sessionManager) {
        list = new ArrayList<>();
        list.add(new LivenessModule());
        list.add(new SignUpModule(services.getSignUpService()));
        list.add(new UserModule(services.getUserService(), sessionManager));
        list.add(new FileModule(services.getFileService(), sessionManager));
        list.add(new GroupModule(services.getGroupService(), sessionManager));
    }

    public List<HttpModule> getList() {
        return list;
    }
}
