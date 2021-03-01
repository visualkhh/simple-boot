package com.simple.boot.web.controller;

import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.web.controller.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.PostMapping;
import com.simple.boot.web.model.User;
import com.simple.boot.web.controller.returns.View;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

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

    @GetMapping("/")
    public View index(Request request, Response response) {
        View view = new View("views/index.html");
        return view;
    }
    @PostMapping("/post")
    public Flux<String> post(Request request, Response response) {
//        request.body(String.class);
//        request.body( )
        return request.bodyFlux(String.class).map(it -> {
            return it + "----";
        });
//       return Flux.just("Hello");
    }
}
