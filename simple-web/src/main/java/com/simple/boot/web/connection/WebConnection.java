package com.simple.boot.web.connection;

import reactor.netty.http.server.HttpServer;

public interface WebConnection {
    HttpServer create();
}
