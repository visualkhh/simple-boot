package com.simple.boot;

import com.simple.boot.bootstrap.SimpleApplication;
import com.simple.boot.bootstrap.SimpleBoot;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class ApplicationTest {

    @Test
    public void start() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        SimpleBoot simpleApplication = new SimpleApplication();
        simpleApplication.run(ApplicationTest.class);
    }
}
