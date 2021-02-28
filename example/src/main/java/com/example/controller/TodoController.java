package com.example.controller;

import com.example.domain.Admin;
import com.example.model.User;
import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.hibernate.HibernateStarter;
import com.simple.boot.web.anno.GetMapping;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.returns.View;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

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
        Session session = hibernateStarter.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Admin admin = new Admin();
        admin.setSeq(1);
        admin.setName("av");
        session.save(admin);
        session.flush();
        session.clear();
        session.getTransaction().commit();
        return "Hello World!";
    }
    @GetMapping("/admins")
    public Admin admins(Request request, Response response){
        Session session = hibernateStarter.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Admin admin = session.find(Admin.class, 1);


        session.getTransaction().commit();
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
