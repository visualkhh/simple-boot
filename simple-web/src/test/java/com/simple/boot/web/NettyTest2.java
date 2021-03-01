package com.simple.boot.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

@Slf4j
class NettyTest2 {

    @Test
    public void serverTest() {
        DisposableServer server = HttpServer.create()   // Prepares an HTTP server ready for configuration
                .port(9999)    // Configures the port number as zero, this will let the system pick up
                // an ephemeral port when binding the server
                .route(routes ->
                        // The server will respond only on POST requests
                        // where the path starts with /test and then there is path parameter
                        routes.post("/test/{param}", (request, response) -> {

                            Flux<String> param = request.receive().asString().map(s -> s + ' ' + request.param("param") + '!');

                            return response.sendString(request.receive()
                                            .asString()
                                            .map(s -> s + ' ' + request.param("param") + '!')
                                            .log("http-server"));
                                }
                        )
                )
                .bindNow();
        server.onDispose()
                .block();
    }
}
