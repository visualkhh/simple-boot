package com.simple.boot.web.communication.netty;

import com.simple.boot.web.communication.Request;
import io.netty.handler.codec.http.QueryStringDecoder;
import reactor.netty.http.server.HttpServerRequest;

import java.util.List;
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

    @Override
    public String uri() {
        return request.uri();
    }

    @Override
    public Map<String, List<String>> queryParameters() {
        QueryStringDecoder query = new QueryStringDecoder(uri());
        return query.parameters();
    }
}
