package com.simple.boot.test;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.GetMapping;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.NettyOutbound;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@Slf4j
@Controller
public class WowController {
    public WowController() {
        log.debug("wow constructor hello Contro ");
    }


    @GetMapping("/hello")
    public NettyOutbound hello(HttpServerRequest request, HttpServerResponse response) {
        return response.sendString(Mono.just("안녕하세요Hello World!"));
    }
    @GetMapping("/index")
    public NettyOutbound index(HttpServerRequest request, HttpServerResponse response) {
        return response.sendString(Mono.just("index World!"));
    }
}
