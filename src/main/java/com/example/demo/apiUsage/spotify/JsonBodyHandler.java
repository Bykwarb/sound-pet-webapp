package com.example.demo.apiUsage.spotify;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {
    private Class<T> tClass;
    public JsonBodyHandler(Class<T> tClass){
        this.tClass = tClass;
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJson(tClass);
    }

    public static <T> HttpResponse.BodySubscriber<T> asJson(Class<T> targetType){
        HttpResponse.BodySubscriber<String> upstream = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
        return HttpResponse.BodySubscribers.mapping(upstream, (String body) ->{
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return objectMapper.readValue(body, targetType);
            }catch (IOException e){
                throw new UncheckedIOException(e);
            }
        });
    }
}
