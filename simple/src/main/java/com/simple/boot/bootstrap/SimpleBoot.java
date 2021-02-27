package com.simple.boot.bootstrap;

import java.lang.reflect.InvocationTargetException;

public interface SimpleBoot {

    public void run(Class startClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
