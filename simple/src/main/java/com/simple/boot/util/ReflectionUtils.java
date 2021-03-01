package com.simple.boot.util;

public class ReflectionUtils {

    public static int superClassSize(Class klass) {
        int i = 0;
        Class superclass = klass.getSuperclass();
        if (null != klass.getSuperclass()) {
            i = i + 1 + superClassSize(superclass);
        }
        return i;
    }
}
