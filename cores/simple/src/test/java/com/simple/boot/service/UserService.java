package com.simple.boot.service;

import com.simple.boot.anno.Service;
import com.simple.boot.model.User;

@Service
public class UserService {

    public User createUser(){
        User user = new User();
        user.setAge(55);
        user.setName("hhhh");
        return user;
    }
}
