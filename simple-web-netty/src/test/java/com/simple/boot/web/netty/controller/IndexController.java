package com.simple.boot.web.netty.controller;


import com.simple.boot.anno.Controller;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.GetMapping;
import com.simple.boot.web.controller.returns.View;

@Controller
public class IndexController {

    @GetMapping("/")
    public String hello(Request request, Response response) {
        return "hello";
    }

    @GetMapping("/index")
    public View index(Request request, Response response) {
        View view = new View("views/index.html");
        view.put("name", "visualkhh");
        return view;
    }
}
