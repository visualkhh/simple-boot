package com.simple.boot.test.controller;

import com.simple.boot.anno.Controller;
import com.simple.boot.test.model.User;
import com.simple.boot.web.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.rtn.View;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Controller
public class WowController {
    public WowController() {
        log.debug("wow constructor hello Contro ");
    }

    @GetMapping("/hello")
    public User hello(Request request, Response response) {
        User user = new User();
        user.setName("hello");
        user.setAge(55);
        return user;
    }
    @GetMapping("/index")
    public View index(Request request, Response response) {
        View view = new View("views/index.html");
        view.put("name", "aaaa");
        return view;
    }
}
