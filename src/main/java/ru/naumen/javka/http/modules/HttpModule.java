package ru.naumen.javka.http.modules;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import ru.naumen.javka.exceptions.JavkaException;

public abstract class HttpModule extends AllDirectives {
    public abstract Route api();
    protected abstract Logger logger();

    Route javkaError(JavkaException javka) {
        logger().error("Handled error", javka);
        return complete(StatusCodes.INTERNAL_SERVER_ERROR, javka.getMessage());
    }

    Route internalError(Throwable th) {
        logger().error("Unhandled error", th);
        return complete(StatusCodes.INTERNAL_SERVER_ERROR, "Произошла внутреняя ошибка");
    }
}
