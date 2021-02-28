package com.simple.boot;

import com.simple.boot.bootstrap.SimpleApplication;
import com.simple.boot.bootstrap.SimpleBoot;
import com.simple.boot.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

@Slf4j
class ApplicationTest {

    @Test
    public void start() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        SimpleBoot simpleApplication = new SimpleApplication();
        simpleApplication.run(ApplicationTest.class);
        UserController sim = simpleApplication.getSim(UserController.class);
        log.info("create user: {}", sim.createUser());;
    }
}
