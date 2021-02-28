package com.simple.boot.web;

import com.simple.boot.bootstrap.SimpleBoot;
import com.simple.boot.web.bootstrap.SimpleWebApplication;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class ApplicationWebTest {

    @Test
    public void start() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException {
        SimpleBoot simpleApplication = new SimpleWebApplication();
        simpleApplication.run(ApplicationWebTest.class);
    }
}
