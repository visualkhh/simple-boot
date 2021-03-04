package com.example.controller;

import com.example.domain.Admin;
import com.example.model.User;
import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.hibernate.HibernateStarter;
import com.simple.boot.throwable.ProcessingException;
import com.simple.boot.web.controller.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.PostMapping;
import com.simple.boot.web.controller.returns.View;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

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
    @GetMapping("/users")
    public List<User> users(Request request, Response response){
        User user = new User();
        user.setAge(1);
        user.setName("name");
        User user2 = new User();
        user2.setAge(1);
        user2.setName("name");
        return Arrays.asList(user,user2);
    }

    @GetMapping("/index")
    public View index(Request request, Response response){
        View view = new View("views/index.html");
        view.put("name", "name");
        return view;
    }

    @PostMapping("/post")
    public String post(Request request, Response response) throws ProcessingException {
        Admin body = request.body(Admin.class);
        return "popopo";
        //        request.body(String.class);
//        request.body( )
//        return request.bodyFlux(String.class).map(it -> {
//            return it + "----";
//        });
//       return Flux.just("Hello");
    }
}
