package com.simple.boot;

import java.lang.reflect.InvocationTargetException;

public interface SimpleBoot {

    public void run(Class startClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    public <T> T getSim(Class<T> klass);
}
