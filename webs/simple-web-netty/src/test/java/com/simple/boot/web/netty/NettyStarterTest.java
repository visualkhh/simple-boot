package com.simple.boot.web.netty;

import com.simple.boot.SimpleApplication;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class NettyStarterTest {

    @Test
    public void test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new SimpleApplication().run(NettyStarterTest.class).join();
    }

}
