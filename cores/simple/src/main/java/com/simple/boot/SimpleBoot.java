package com.simple.boot;

import java.lang.reflect.InvocationTargetException;

public interface SimpleBoot {

    public SimpleBoot run(Class startClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    public void join();

    public <T> T getSim(Class<T> klass);
}
