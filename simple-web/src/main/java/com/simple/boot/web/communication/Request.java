package com.simple.boot.web.communication;

import reactor.core.publisher.Flux;
import reactor.util.annotation.Nullable;

import java.util.List;
import java.util.Map;

public interface Request {
    public String param(String key);
    public Map<String, String> params();
    public String path();
    public String uri();
    public <T> T body(Class<T> klass);
    public <T> Flux<T> bodyFlux(Class<T> klass);
    public Map<String, List<String>> queryParameters();
}
