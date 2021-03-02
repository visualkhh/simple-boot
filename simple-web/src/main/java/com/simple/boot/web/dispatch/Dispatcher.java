package com.simple.boot.web.dispatch;

import com.simple.boot.anno.Controller;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.web.anno.ExceptionHandler;
import com.simple.boot.web.anno.FilterAfterHandler;
import com.simple.boot.web.anno.FilterBeforeHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void met(){

    }

    protected abstract void mapping() throws Exception;
}
