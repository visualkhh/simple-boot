package com.simple.boot.web.controller;

import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.web.controller.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.model.User;
import com.simple.boot.web.controller.returns.View;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

    @GetMapping("/")
    public View index(Request request, Response response) {
        View view = new View("views/index.html");
        return view;
    }
}
