package com.simple.boot.web.communication;

import reactor.netty.http.server.HttpServerRequest;

import java.util.Map;

public class NettyRequest implements Request {

    private final HttpServerRequest request;

    public NettyRequest(HttpServerRequest request) {
        this.request = request;
    }

    @Override
    public String param(String key) {
        return request.param(key);
    }

    @Override
    public Map<String, String> params() {
        return request.params();
    }

    @Override
    public String path() {
        return request.path();
    }
}
