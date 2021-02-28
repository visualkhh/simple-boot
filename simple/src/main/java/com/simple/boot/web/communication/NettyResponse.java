package com.simple.boot.web.communication;

import reactor.netty.http.server.HttpServerResponse;

public class NettyResponse implements Response {

    HttpServerResponse response;

    public NettyResponse(HttpServerResponse response) {
        this.response = response;
    }

    @Override
    public void header(String header, String value) {
        response.header(header, value);
    }

    @Override
    public void status(int value) {
        response.status(value);
    }
}
