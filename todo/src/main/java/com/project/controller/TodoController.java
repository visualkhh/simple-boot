package com.project.controller;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TodoController {

    @GetMapping("/hello")
    public String hello(Request request, Response response){
        log.debug("==========");
        return "Hello World!";
    }
    @GetMapping("/wow")
    public String wow(Request request, Response response){
        return "wow";
    }
}
