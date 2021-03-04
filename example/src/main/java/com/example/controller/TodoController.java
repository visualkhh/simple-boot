package com.example.controller;

import com.example.domain.Admin;
import com.example.model.User;
import com.example.service.UserService;
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
    private final UserService userService;

    @Injection
    public TodoController(HibernateStarter hibernateStarter, UserService userService) {
        this.hibernateStarter = hibernateStarter;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String admin(Request request, Response response){
        Admin admin = new Admin();
        admin.setSeq(1);
        admin.setName("av");
        hibernateStarter.save(admin);
        return "good";
    }

    @PostMapping("/admin")
    public void saveAdmin(Request request, Response response) throws ProcessingException {
        Admin admin = request.body(Admin.class);
        hibernateStarter.save(admin);
    }

    @GetMapping("/admins")
    public List<Admin> admins(Request request, Response response){
        List<Admin> admins = hibernateStarter.resultList(Admin.class, (a) -> {
        });
        return admins;
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
        return userService.users();
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
