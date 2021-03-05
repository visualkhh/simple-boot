package com.simple.boot.util;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Predicate;

public class ProxyUtils {
    public static Object setInterfaceProxy(Class inter, InvocationHandler handler) {
        return setInterfaceProxy(inter, handler, Object.class);
    }

    public static <T> T setInterfaceProxy(Class inter, InvocationHandler handler, Class<T> rc) {
        T t = (T) Proxy.newProxyInstance(inter.getClassLoader(), new Class[]{inter}, handler);
        return t;
    }
    public static <T> T newInstanceMethodProxy(Class<T> objClass, MethodHandler handler) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return newInstanceMethodProxy(objClass, new Class[0], new Object[0], handler, null);
    }
    public static <T> T newInstanceMethodProxy(Class<T> objClass, Class[] paramTypes, Object[] args, MethodHandler handler) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return newInstanceMethodProxy(objClass, paramTypes, args, handler, null);
    }
    public static <T> T newInstanceMethodProxy(Class<T> objClass, Class[] paramTypes, Object[] args, MethodHandler handler, MethodFilter test) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(objClass);
        if (null != test) {
            factory.setFilter(test);
        }
//        factory.setFilter(
//                new MethodFilter() {
//                    @Override
//                    public boolean isHandled(Method method) {
//                        return Modifier.isAbstract(method.getModifiers());
//                    }
//                }
//        );

//        MethodHandler handler = new MethodHandler() {
//            @Override
//            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
//                System.out.println("Handling " + thisMethod + " via the method handler");
//                proceed.invoke(self, args);
//                return null;
//            }
//        };

        T instance = (T) factory.create(paramTypes, args, handler);
        return instance;
    }
}
