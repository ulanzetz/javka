package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.exceptions.NoPermissionException;
import ru.naumen.javka.session.SessionManager;

import java.util.HashMap;

public class SignUpModule extends HttpModule {
    private Logger logger;
    private SessionManager sessionManager;

    public SignUpModule(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        logger = LoggerFactory.getLogger(SignUpModule.class);
    }

    @Override
    public Route api() {
        return pathPrefix("sign_up", () -> parameter("id", id -> parameter("password", password -> {
            // TODO auth
            if (password.equals("kek"))
                try {
                    return jsonComplete(new HashMap<String, String>() {
                        {
                            put("session", sessionManager.session(Long.parseLong(id)));
                        }
                    });
                } catch (Throwable th) {
                    return internalError(th);
                }
            else
                return javkaError(new NoPermissionException());
        })));
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
