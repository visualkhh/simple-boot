package com.example.controller;

import com.example.domain.Admin;
import com.example.model.User;
import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.hibernate.HibernateStarter;
import com.simple.boot.web.controller.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.returns.View;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TodoController {
    private final HibernateStarter hibernateStarter;

    @Injection
    public TodoController(HibernateStarter hibernateStarter) {
        this.hibernateStarter = hibernateStarter;
    }

    @GetMapping("/admin")
    public String admin(Request request, Response response){
        Admin admin = new Admin();
        admin.setSeq(1);
        admin.setName("av");
        hibernateStarter.save(admin);
        return "good";
    }

    @GetMapping("/admins")
    public Admin admins(Request request, Response response){
        Admin admin = hibernateStarter.find(Admin.class, 1);
        return admin;
    }

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
