package com.simple.boot.db.hibernate;

import com.simple.boot.SimpleApplication;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class HibernateConfigTest {

    @Test
    public void test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new SimpleApplication().run(HibernateConfigTest.class);
    }
}
