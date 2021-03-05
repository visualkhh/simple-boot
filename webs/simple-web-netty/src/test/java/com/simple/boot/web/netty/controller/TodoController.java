package com.simple.boot.web.netty.controller;


import com.simple.boot.anno.Controller;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.GetMapping;

@Controller
public class TodoController {

    @GetMapping("/todo")
    public String hello(Request request, Response response) {
        return "hello";
    }
}
