package com.simple.boot.web.netty.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.throwable.ProcessingException;
import com.simple.boot.web.controller.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.PostMapping;
import com.simple.boot.web.controller.returns.View;
import com.simple.boot.web.netty.model.User;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class WowController {

    private final WowService wowService;

    @Injection
    public WowController(WowService wowService) {
        this.wowService = wowService;
    }

    @GetMapping("/users")
    public List<User> users(Request request, Response response) {
        return wowService.createUsers();
    }
    @GetMapping("/test")
    public String test(Request request, Response response) {
        Map<String, List<String>> stringListMap = request.queryParameters();
        int ii = Integer.parseInt(stringListMap.get("d").get(0));
        int a = 5 / ii;
        return "test " + a;
    }



    @PostMapping("/post")
    public String post(Request request, Response response) throws ProcessingException {
        return "-a-sdasdsa   " + request.body(String.class);
        //        request.body(String.class);
//        request.body( )
//        return request.bodyFlux(String.class).map(it -> {
//            return it + "----";
//        });
//       return Flux.just("Hello");
    }
}
