package ru.naumen.javka.http.modules;

import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.naumen.javka.exceptions.NoPermissionException;
import ru.naumen.javka.services.SignUpService;
import ru.naumen.javka.services.UserService;

import java.util.HashMap;
import java.util.Optional;

public class SignUpModule extends HttpModule {
    private SignUpService signUpService;
    private UserService userService;
    private Logger logger;

    public SignUpModule(SignUpService signUpService) {
        this.signUpService = signUpService;
        logger = LoggerFactory.getLogger(SignUpModule.class);
    }

    @Override
    public Route api() throws NullPointerException {
        Route signUp = pathPrefix("sign_up", () -> parameter("name", name -> parameter("password", password -> {
            try {
               return jsonComplete(signUpService.create(name, password));
            } catch (Throwable th) {
                return internalError(th);
            }
        })));

        Route signIn = pathPrefix("sign_in", () -> parameter("id", id -> parameter("password", password -> {
            try {
                Optional<String> session = signUpService.session(Long.parseLong(id), password);
                if (session.isPresent())
                    return jsonComplete(new HashMap<String, String>() {
                        {
                            put("session", session.get());
                        }
                    });
                else return javkaError(new NoPermissionException());
            } catch (Throwable th) {
                return internalError(th);
            }
        })));

        return concat(signIn, signUp);
    }

    @Override
    protected Logger logger() {
        return logger;
    }
}
