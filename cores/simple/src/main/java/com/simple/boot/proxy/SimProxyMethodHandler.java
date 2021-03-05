package com.simple.boot.proxy;


import com.simple.boot.anno.transaction.Transactional;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.transaction.TransactionManager;
import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class SimProxyMethodHandler implements MethodHandler {

    private final SimstanceManager simstanceManager;

    public SimProxyMethodHandler(SimstanceManager simstanceManager) {
        this.simstanceManager = simstanceManager;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {

        //transaction managers
        forEach(thisMethod, Transactional.class, TransactionManager.class, (a, o) -> o.beginTransaction());
        System.out.println("Handling " + thisMethod + " via the method handler");
        try {
            Object rtn = proceed.invoke(self, args);
            forEach(thisMethod, Transactional.class, TransactionManager.class, (a, o) -> o.commit());
            return rtn;
        } catch (Exception e) {
            forEach(thisMethod, Transactional.class, TransactionManager.class, (a, o) -> o.rollback());
            throw e;
        } finally {
        }
    }


    public <T extends Annotation, E> void forEach(Method method, Class<T> methodAnno, Class<E> executor, BiConsumer<T, E> executorCunsumer) {
        T annotation = method.getAnnotation(methodAnno);
        if (null != annotation) {
            LinkedHashMap<Class<E>, E> findExecutors = this.simstanceManager.getSimsNotNullObjAndIsAssignableFrom(executor);
            if (null != findExecutors && findExecutors.size() > 0) {
                findExecutors.entrySet().forEach(it -> executorCunsumer.accept(annotation, it.getValue()));
            }
        }

    }
}
