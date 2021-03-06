package com.simple.boot.proxy;


import com.simple.boot.anno.transaction.Transactional;
import com.simple.boot.aop.Aop;
import com.simple.boot.aop.anno.AopAfter;
import com.simple.boot.aop.anno.AopBefore;
import com.simple.boot.aop.anno.AopException;
import com.simple.boot.aop.anno.AopFinally;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.transaction.TransactionManager;
import com.simple.boot.util.StringUtils;
import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimProxyMethodHandler implements MethodHandler {

    private final SimstanceManager simstanceManager;

    public SimProxyMethodHandler(SimstanceManager simstanceManager) {
        this.simstanceManager = simstanceManager;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {

        //transaction managers
//        System.out.println("Handling " + thisMethod + " via the method handler");

        try {
            forEach(thisMethod, Transactional.class, TransactionManager.class, (a, o) -> o.beginTransaction());
            findMethodToAopForEach(AopAfter.class, args, (a) -> StringUtils.isMatches(thisMethod.toString(), a.regex())).forEach(it -> it.invokeNoThrows());

            Object rtn = proceed.invoke(self, args);

            findMethodToAopForEach(AopBefore.class, args, (a) -> StringUtils.isMatches(thisMethod.toString(), a.regex())).forEach(it -> it.invokeNoThrows());
            forEach(thisMethod, Transactional.class, TransactionManager.class, (a, o) -> o.commit());
            return rtn;
        } catch (Exception e) {
            forEach(thisMethod, Transactional.class, TransactionManager.class, (a, o) -> o.rollback());
            findMethodToAopForEach(AopException.class, args, e, (a) -> StringUtils.isMatches(thisMethod.toString(), a.regex())).forEach(it -> it.invokeNoThrows());
            throw e;
        } finally {
            findMethodToAopForEach(AopFinally.class, args, (a) -> StringUtils.isMatches(thisMethod.toString(), a.regex())).forEach(it -> it.invokeNoThrows());
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


    public <T extends Annotation> List<Aop> findMethodToAopForEach(Class<T> targetMethodAnnotahion, Object[] args, Predicate<T> filter) {
        return findMethodForEach(targetMethodAnnotahion, filter).stream().map(it -> Aop.builder().method(it.getKey()).object(it.getValue()).args(args).build()).collect(Collectors.toList());
    }
    public <T extends Annotation> List<Aop> findMethodToAopForEach(Class<T> targetMethodAnnotahion, Object[] args, Throwable throwable, Predicate<T> filter) {
        return findMethodForEach(targetMethodAnnotahion, filter).stream().map(it -> Aop.builder().method(it.getKey()).object(it.getValue()).args(args).throwable(throwable).build()).collect(Collectors.toList());
    }

    public <T extends Annotation> List<Map.Entry<Method, Object>> findMethodForEach(Class<T> targetMethodAnnotahion, Predicate<T> filter) {
        LinkedHashMap<Method, Object> methodAnnotation = this.simstanceManager.getMethodAnnotation(targetMethodAnnotahion);
        return methodAnnotation.entrySet().stream().filter(it -> {
            T annotation = it.getKey().getAnnotation(targetMethodAnnotahion);
            return null != annotation && filter.test(annotation);
        }).collect(Collectors.toList());
    }
}
