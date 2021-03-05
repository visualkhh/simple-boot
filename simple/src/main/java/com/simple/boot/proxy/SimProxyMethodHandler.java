package com.simple.boot.proxy;


import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;

public class SimProxyMethodHandler implements MethodHandler {
    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        System.out.println("Handling " + thisMethod + " via the method handler");
        return proceed.invoke(self, args);
    }
}
