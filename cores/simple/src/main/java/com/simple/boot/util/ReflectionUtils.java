package com.simple.boot.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils extends org.reflections.ReflectionUtils {

    public static int superClassSize(Class klass) {
        int i = 0;
        Class superclass = klass.getSuperclass();
        if (null != klass.getSuperclass()) {
            i = i + 1 + superClassSize(superclass);
        }
        return i;
    }

    public static Object invoke(Method method, Object obj, Object ...params) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(obj, params);
    }
    public static Object newInstance(Constructor constructor, Object ...params) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        return constructor.newInstance(params);
    }
}
