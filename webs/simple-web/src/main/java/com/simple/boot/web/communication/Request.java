package com.simple.boot.web.communication;


import com.simple.boot.throwable.ProcessingException;
import com.simple.boot.web.http.HttpMethod;

import java.util.List;
import java.util.Map;

public interface Request {
    public String param(String key);

    public HttpMethod method();

    public Map<String, String> params();

    public String path();

    public String uri();

    public <T> T body(Class<T> klass) throws ProcessingException;

    //    public <T> Flux<T> bodyFlux(Class<T> klass);
    public Map<String, List<String>> queryParameters();

    public Map<String, String> queryFirstParameters();
}
