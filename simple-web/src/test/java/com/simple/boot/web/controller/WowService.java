package com.simple.boot.web.controller;

import com.simple.boot.anno.Injection;
import com.simple.boot.anno.Service;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.web.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class WowService {

    public List<User> createUsers() {
        return Arrays.asList(
                User.builder().name("aUser").age(1).build(),
                User.builder().name("bUser").age(2).build(),
                User.builder().name("cUser").age(3).build()
        );
    }
}
