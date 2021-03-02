package com.simple.boot.web.reactor.netty;

import com.simple.boot.SimpleApplication;
import com.simple.boot.SimpleBoot;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class ApplicationWebTest {

    @Test
    public void start() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, InterruptedException {
        SimpleBoot simpleApplication = new SimpleApplication();
        simpleApplication.run(ApplicationWebTest.class);
    }
}
