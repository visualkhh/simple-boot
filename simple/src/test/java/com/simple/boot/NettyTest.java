package com.simple.boot;

import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.util.concurrent.TimeUnit;

@Slf4j
class NettyTest {

    @Test
    public void serverTest() {
        log.debug("-------");
        DisposableServer server =
                HttpServer.create()
//                        .accessLog(true, AccessLogFactory.createFilter(p -> {
//                            return !String.valueOf(p.uri()).startsWith("/health/");
//                        }))
//                        .compress(true)
                        .host("localhost")
                        .port(8080)
//                        .handle((request, response) -> {
//                            return request.receive().then();
//                        })
//                        .route(routes ->
//                                        routes
//                                                .get("/hello", (request, response) -> {
//                                                    //                                            return response.status(HttpResponseStatus.OK)
//                                                    //                                                    .header(HttpHeaderNames.CONTENT_LENGTH, "12")
//                                                    //                                                    .sendString(Mono.just("Hello World!"));
//                                                    return response.sendString(Mono.just("Hello World!"));
//                                                })
//                                                .post("/echo", (request, response) -> response.send(request.receive().retain()))
//                                                .get("/path/{param}", (request, response) -> response.sendString(Mono.just(request.param("param"))))
//                                                .ws("/ws", (wsInbound, wsOutbound) -> {
////                                                    wsOutbound.send(wsInbound.receive().retain());
//                                                    ByteBufFlux retain = wsInbound.receive().retain();
//                                                    return wsOutbound.send(retain);
////                                                    return wsOutbound.sendString(Mono.just("asdasd"));
//                                                })
//                                                .get("/**", (request, response) -> {
//                                                    return response;
//                                                })
//
//                        )
//                        .handle((request, response) -> {
//                            response.responseHeaders().add("Access-Control-Allow-Origin", "*");
//                            return request.receive().then();
//                        })
                        .route(routes -> {
                            routes.get("/hello", (request, response) -> {
                                return response.sendString(Mono.just("Hello World!"));
                            });
                            routes.andThen((r) -> {
                                return r;
                            });
                            routes.index((r,rr)->{
                                return rr;
                            });
                            routes.head("/**", (request, response) -> {
                                return response.sendString(Mono.just("Hello World!"));
                            });
//                            routes.route((r, rr) -> {
//                                return new Publisher<Object>() {
//                                    @Override
//                                    public void subscribe(Subscriber<? super Object> s) {
//                                        return null;
//                                    }
//                                };
//                            });
                        })
//                        .doOnConnection(conn ->
//                                conn.addHandler(new ReadTimeoutHandler(10, TimeUnit.SECONDS)))
//                        .doOnChannelInit((observer, channel, remoteAddress) ->
//                                channel.pipeline()
//                                        .addFirst(new LoggingHandler("reactor.netty.examples")))
//                        .doOnUnbound((d) -> {
//                            log.debug("{}", d);
//                        })
//                        .handle((request, response) -> {
//                            return response.sendString(Mono.just("hello"));
////                            response.responseHeaders().add("Access-Control-Allow-Origin", "*");
////                            response.addHeader("Access-Control-Allow-Origin", "*");
////                            return response;
//                        })
                        .wiretap(true)
                        .bindNow();

        server.onDispose()
                .block();
    }
}
