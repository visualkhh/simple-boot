package com.simple.boot.hibernate;

import com.simple.boot.bootstrap.SimpleApplication;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class HibernateConfigTest {

    @Test
    public void test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new SimpleApplication().run(HibernateConfigTest.class);
    }
}
