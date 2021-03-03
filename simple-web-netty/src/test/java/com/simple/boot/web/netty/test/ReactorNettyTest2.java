//package com.simple.boot.web.netty.test;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import reactor.core.publisher.Flux;
//import reactor.netty.DisposableServer;
//import reactor.netty.http.server.HttpServer;
//
//@Slf4j
//class ReactorNettyTest2 {
//
//    @Test
//    public void serverTest() {
//        DisposableServer server = HttpServer.create()   // Prepares an HTTP server ready for configuration
//                .port(9999)    // Configures the port number as zero, this will let the system pick up
//                // an ephemeral port when binding the server
//                .route(routes ->
//                                // The server will respond only on POST requests
//                                // where the path starts with /test and then there is path parameter
//                                routes.post("/test/{param}", (request, response) -> {
//
////                                            return request.receive().then(response.status(300).send());
//                                    return response.sendString(
//                                            request.receive().asString().map(it -> it+"---")
//                                    ).then((it) ->{
//                                        log.info("------");
//                                    });//.then(response.status(300).send());
////                                            return request.receive().asString().map(it -> it+"---").then(response.status(300).send());
////                                            Flux<String> param = request.receive().asString().map(s -> s + ' ' + request.param("param") + '!');
////                            response.status(500);
////                                            return response.sendString(request.receive().asString()
////                                                            .map(s -> {
//////                                                response.status(500);
//////                                                response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
////                                                                return s + ' ' + request.param("param") + '!';
////                                                            })
////                                                            .log("http-server")
////                                            );
//                                        }
//                                )
//                )
//                .bindNow();
//        server.onDispose()
//                .block();
//    }
//}
