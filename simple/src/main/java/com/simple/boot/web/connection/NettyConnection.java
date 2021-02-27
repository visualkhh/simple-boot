package com.simple.boot.web.connection;

import reactor.netty.http.server.HttpServer;

public class NettyConnection implements WebConnection {

    public HttpServer create() {
        return HttpServer.create();
    }
}
