package com.example.service;

import com.example.model.User;
import com.simple.boot.anno.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    public List<User> users() {
        User user = new User();
        user.setAge(1);
        user.setName("name1");
        User user2 = new User();
        user2.setAge(2);
        user2.setName("name2");
        return Arrays.asList(user, user2);
    }

}
