package com.project.controller;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.GetMapping;
import reactor.core.publisher.Mono;
import reactor.netty.NettyOutbound;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@Controller
public class TodoController {

    @GetMapping("/hello")
    public NettyOutbound hello(HttpServerRequest request, HttpServerResponse response){
        return response.sendString(Mono.just("Hello World!"));
    }
    @GetMapping("/wow")
    public NettyOutbound wow(HttpServerRequest request, HttpServerResponse response){
        return response.sendString(Mono.just("wow"));
    }
}
