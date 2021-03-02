package com.simple.boot.web.dispatch;

import com.simple.boot.anno.Controller;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.web.anno.ExceptionHandler;
import com.simple.boot.web.anno.FilterAfterHandler;
import com.simple.boot.web.anno.FilterBeforeHandler;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.model.MethodObjectSet;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Slf4j
public abstract class Dispatcher {

    private final LinkedHashMap<Method, Object> exceptionHandlers;
    private final LinkedHashMap<Method, Object> filterBeforeHandlers;
    private final LinkedHashMap<Method, Object> filterAfterHandlers;
    private final LinkedHashMap<Class, Object> controllers;

    public Dispatcher(SimstanceManager simstanceManager) {
        exceptionHandlers = simstanceManager.getMethodAnnotation(ExceptionHandler.class, Comparator.comparingInt(ExceptionHandler::order));
        filterBeforeHandlers = simstanceManager.getMethodAnnotation(FilterBeforeHandler.class, Comparator.comparingInt(FilterBeforeHandler::order));
        filterAfterHandlers = simstanceManager.getMethodAnnotation(FilterAfterHandler.class, Comparator.comparingInt(FilterAfterHandler::order));

        controllers = simstanceManager.getSims().entrySet().stream().filter(it -> it.getKey().isAnnotationPresent(Controller.class)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
    }

    public <T extends Annotation> Optional<MethodObjectSet> getControllerMappingAnnotaion(Class<T> annotations, Predicate<T> test){
        for (Map.Entry<Class, Object> it : controllers.entrySet()) {
            Optional<Method> first = Stream.of(it.getKey().getDeclaredMethods()).filter(sit -> test.test(sit.getAnnotation(annotations))).findFirst();
            if(first.isPresent()){
                return Optional.of(MethodObjectSet.builder().method(first.get()).klass(it.getKey()).object(it.getValue()).build());
            }
        }
        return Optional.empty();
//        return controllers.entrySet().stream().filter(it -> {
//            return Stream.of(it.getClass().getDeclaredMethods()).map(sit -> sit.getAnnotation(annotations.getClass())).filter(sit -> test.test((T) sit)).findFirst().isPresent();
//        }).map(it -> {
//            return new MethodSet(it.getKey(), it.getValue(), it.getKey());
//        }).findFirst();
    }

    public abstract void executeMapping(Request request, Response response);
}
