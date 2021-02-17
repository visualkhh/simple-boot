package com.simple.boot;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class NettyTest {

    @Test
    public void serverTest() {
        log.debug("-------");
        DisposableServer server =
                HttpServer.create()
                        .host("localhost")
                        .port(8080)
                        .route(routes ->
                                routes.get("/hello",
                                        (request, response) -> {
//                                            return response.status(HttpResponseStatus.OK)
//                                                    .header(HttpHeaderNames.CONTENT_LENGTH, "12")
//                                                    .sendString(Mono.just("Hello World!"));
                                            return response.sendString(Mono.just("Hello World!"));
                                        })
                                        .post("/echo",
                                                (request, response) -> response.send(request.receive().retain()))
                                        .get("/path/{param}",
                                                (request, response) -> response.sendString(Mono.just(request.param("param"))))
                                        .ws("/ws",
                                                (wsInbound, wsOutbound) -> {
//                                                    wsOutbound.send(wsInbound.receive().retain());
                                                    ByteBufFlux retain = wsInbound.receive().retain();
                                                    return wsOutbound.send(retain);
//                                                    return wsOutbound.sendString(Mono.just("asdasd"));
                                                }))
                        .bindNow();

        server.onDispose()
                .block();
    }
}
