package ru.naumen.javka.http.modules;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.StringUnmarshallers;
import akka.http.javadsl.marshalling.Marshaller;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import ru.naumen.javka.exceptions.JavkaException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class HttpModule extends AllDirectives {
    public abstract Route api();

    protected abstract Logger logger();

    private ObjectMapper objectMapper = new ObjectMapper()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    Route javkaError(JavkaException javka) {
        logger().error("Handled error", javka);
        return complete(StatusCodes.INTERNAL_SERVER_ERROR, javka.getMessage());
    }

    Route internalError(Throwable th) {
        logger().error("Unhandled error", th);
        return complete(StatusCodes.INTERNAL_SERVER_ERROR, "Произошла внутреняя ошибка");
    }

    <T> Route jsonComplete(T obj) {
        return complete(StatusCodes.OK, obj, Jackson.marshaller(objectMapper));
    }

    Route binaryComplete(byte[] array) {
        return complete(StatusCodes.OK, array, Marshaller.byteArrayToEntity());
    }

    Route ok() {
        return jsonComplete(new HashMap<String, String>() {
            {
                put("status", "OK");
            }
        });
    }

    Route longParam(String name, Function<Long, Route> inner) {
        return parameter(StringUnmarshallers.LONG, name, inner);
    }

    Route longParams(String name, Function<List<Long>, Route> inner) {
        return parameterList(StringUnmarshallers.LONG, name, inner);
    }

    Route uuidParam(String name, Function<UUID, Route> inner) {
        return parameter(StringUnmarshallers.UUID, name, inner);
    }

    Route optUuidParam(String name, Function<Optional<UUID>, Route> inner) {
        return parameterOptional(StringUnmarshallers.UUID, name, inner);
    }
}
