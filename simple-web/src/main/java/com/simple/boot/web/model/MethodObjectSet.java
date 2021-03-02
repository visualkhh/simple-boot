package com.simple.boot.web.model;

import lombok.Builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Builder
public class MethodObjectSet {
    Class klass;
    Object object;
    Method method;

    public Object invoke(Object ...parameter) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(object, parameter);
    }
}
