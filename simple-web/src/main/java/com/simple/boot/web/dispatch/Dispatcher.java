package com.simple.boot.web.dispatch;

import java.lang.reflect.InvocationTargetException;

public interface Dispatcher {
    void mapping() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
