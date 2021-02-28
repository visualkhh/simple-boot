package com.simple.boot.web.connection.netty;

import com.simple.boot.web.connection.WebConnection;
import reactor.netty.http.server.HttpServer;

public class NettyConnection implements WebConnection {

    public HttpServer create() {
        return HttpServer.create();
    }
}
