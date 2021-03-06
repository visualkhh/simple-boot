package com.simple.boot.model;

import com.simple.boot.util.ReflectionUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Builder
@Getter
@Setter
public class MethodObjectSet {

    Class klass;
    Object object;
    Method method;

    public Object invoke(Object... parameter) throws InvocationTargetException, IllegalAccessException {
        return ReflectionUtils.invoke(method, object, parameter);
    }


    public Class getKlass() {
        if (null != klass) {
            return this.klass;
        } else if (null != this.object) {
            return this.object.getClass();
        } else {
            return null;
        }
    }
}
