package com.github.maximtereshchenko.airquality;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;

final class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final TypeReference<T> typeReference;

    JsonBodyHandler(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
            HttpResponse.BodySubscribers.ofByteArray(),
            bytes -> {
                try {
                    return objectMapper.readValue(bytes, typeReference);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        );
    }
}
