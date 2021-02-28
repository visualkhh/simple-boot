package com.exemple.controller;

import com.exemple.model.User;
import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.returns.View;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TodoController {

    @GetMapping("/hello")
    public String hello(Request request, Response response){
        return "Hello World!";
    }
    @GetMapping("/user")
    public User user(Request request, Response response){
        User user = new User();
        user.setAge(1);
        user.setName("name");
        return user;
    }
    @GetMapping("/index")
    public View index(Request request, Response response){
        View view = new View("views/index.html");
        view.put("name", "name");
        return view;
    }
}
