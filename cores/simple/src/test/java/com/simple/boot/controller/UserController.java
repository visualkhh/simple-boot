package com.simple.boot.controller;

import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.model.User;
import com.simple.boot.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

    private final UserService userService;

    @Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User createUser(){
        return userService.createUser();
    }
}
