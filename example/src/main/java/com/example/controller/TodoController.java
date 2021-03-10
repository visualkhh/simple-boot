package com.example.controller;

import com.example.domain.Admin;
import com.example.model.User;
import com.example.service.AdminService;
import com.example.service.UserService;
import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.throwable.ProcessingException;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.GetMapping;
import com.simple.boot.web.controller.anno.PostMapping;
import com.simple.boot.web.controller.returns.View;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class TodoController {

    private final UserService userService;
    private final AdminService adminService;

    @Injection
    public TodoController(AdminService adminService, UserService userService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String hello(Request request, Response response) {
        return "Hello World!";
    }

    @GetMapping("/admins")
    public List<Admin> admins(Request request, Response response) {
        return adminService.admins();
    }

    @PostMapping("/admin")
    public Serializable saveAdmin(Request request, Response response) throws ProcessingException {
        Admin admin = request.body(Admin.class);
        if (null == admin.getName()) {
            admin.setName(String.valueOf(System.currentTimeMillis()));
        }
        Serializable save = adminService.save(admin);
        return save;
    }


    @GetMapping("/i18n")
    public Map<String, String> i18n(Request request, Response response) {
        String lang = request.queryFirstParameters().get("lang");
        Map<String, String> langs = new HashMap<>();
        if ("ko".equals(lang)) {
            langs.put("hello", "안녕하세요");
            langs.put("ok", "확인");
            langs.put("close", "닫기");
            langs.put("wait_user", "고객 기다려주세요");
        } else {
            langs.put("hello", "hello");
            langs.put("ok", "ok");
            langs.put("close", "close");
            langs.put("wait_user", "wait user");
        }
        return langs;
    }


    @GetMapping("/user")
    public User user(Request request, Response response) {
        User user = new User();
        user.setAge(1);
        user.setName("name");
        return user;
    }


    @GetMapping("/users")
    public List<User> users(Request request, Response response) {
        return userService.users();
    }


    @GetMapping("/")
    public View index(Request request, Response response) {
        View view = new View("views/index.html");
        view.put("name", "demo page");
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
