package com.simple.boot.web.dispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.simple.boot.anno.Config;
import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Injection;
import com.simple.boot.anno.PostConstruct;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.util.ReflectionUtils;
import com.simple.boot.web.anno.ExceptionHandler;
import com.simple.boot.web.anno.FilterAfterHandler;
import com.simple.boot.web.anno.FilterBeforeHandler;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.*;
import com.simple.boot.web.controller.returns.View;
import com.simple.boot.web.http.HttpHeaderNames;
import com.simple.boot.web.http.HttpHeaderValues;
import com.simple.boot.web.http.HttpMethod;
import com.simple.boot.web.http.HttpStatus;
import com.simple.boot.web.model.MethodObjectSet;
import com.simple.boot.web.throwable.NoSurchHttpMethodError;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Slf4j
@Config
public class Dispatcher {

    private LinkedHashMap<Method, Object> exceptionHandlers;
    private LinkedHashMap<Method, Object> filterBeforeHandlers;
    private LinkedHashMap<Method, Object> filterAfterHandlers;
    private LinkedHashMap<Class, Object> controllers;

    private final ObjectMapper mapper;

    public Dispatcher() {
        this.mapper = new ObjectMapper();
    }

    @PostConstruct
    @Injection
    public void post(SimstanceManager simstanceManager) {
        exceptionHandlers = simstanceManager.getMethodAnnotation(ExceptionHandler.class);
        filterBeforeHandlers = simstanceManager.getMethodAnnotation(FilterBeforeHandler.class, Comparator.comparingInt(FilterBeforeHandler::order));
        filterAfterHandlers = simstanceManager.getMethodAnnotation(FilterAfterHandler.class, Comparator.comparingInt(FilterAfterHandler::order));
        controllers = simstanceManager.getSims().entrySet().stream().filter(it -> it.getKey().isAnnotationPresent(Controller.class)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
    }


    public <T extends Annotation> Optional<MethodObjectSet> getControllerMappingAnnotaion(Class<T> annotations, Predicate<T> test) {
        for (Map.Entry<Class, Object> it : controllers.entrySet()) {
            //List<Method> methods = Stream.of(it.getKey().getDeclaredMethods()).filter(sit -> test.test(sit.getAnnotation(annotations))).collect(Collectors.toList());
            Optional<Method> first = Stream.of(it.getKey().getDeclaredMethods()).filter(sit -> sit.isAnnotationPresent(annotations) && test.test(sit.getAnnotation(annotations))).map(Optional::ofNullable).findFirst().flatMap(Function.identity());
            if (first.isPresent()) {
                return Optional.of(MethodObjectSet.builder().method(first.get()).klass(it.getKey()).object(it.getValue()).build());
            }
        }
        return Optional.empty();
    }

    public <T extends Throwable> Optional<MethodObjectSet> geExceptionHandlerAnnotaion(Class<T> exceptionClass) {
        Optional<Map.Entry<Method, Object>> first = exceptionHandlers.entrySet().stream()
                .filter(it -> it.getKey().getAnnotation(ExceptionHandler.class).value().isAssignableFrom(exceptionClass))
                .sorted((a, b) -> {
                    Class<? extends Throwable> aClass = a.getKey().getAnnotation(ExceptionHandler.class).value();
                    Class<? extends Throwable> bClass = b.getKey().getAnnotation(ExceptionHandler.class).value();
                    return ReflectionUtils.superClassSize(bClass) - ReflectionUtils.superClassSize(aClass);
                })
                .map(Optional::ofNullable)
                .findFirst().flatMap(Function.identity());
        if (first.isPresent()) {
            Map.Entry<Method, Object> excpetionEntry = first.get();
            return Optional.of(MethodObjectSet.builder().method(excpetionEntry.getKey()).object(excpetionEntry.getValue()).build());
        } else {
            return Optional.empty();
        }
    }

    public final void executeMapping(Request request, Response response) {
        Optional<MethodObjectSet> method = null;
        if (HttpMethod.GET == request.method()) {
            method = getControllerMappingAnnotaion(GetMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.POST == request.method()) {
            method = getControllerMappingAnnotaion(PostMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.PUT == request.method()) {
            method = getControllerMappingAnnotaion(PutMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.DELETE == request.method()) {
            method = getControllerMappingAnnotaion(DeleteMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.OPTIONS == request.method()) {
            method = getControllerMappingAnnotaion(OptionsMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.HEAD == request.method()) {
            method = getControllerMappingAnnotaion(HeadMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.PATCH == request.method()) {
            method = getControllerMappingAnnotaion(PatchMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.TRACE == request.method()) {
            method = getControllerMappingAnnotaion(TraceMapping.class, (a) -> a.value().equals(request.path()));
        } else if (HttpMethod.CONNECT == request.method()) {
            method = getControllerMappingAnnotaion(ConnectMapping.class, (a) -> a.value().equals(request.path()));
        } else {
            method = Optional.empty();
        }
        try {
            if (!method.isPresent()) {
                throw new NoSurchHttpMethodError();
            }
            //before
            for (Map.Entry<Method, Object> it : getFilterBeforeHandlers().entrySet()) {
                it.getKey().invoke(it.getValue(), request, response);
            }
            Object rtn = method.get().invoke(request, response);

            //after
            for (Map.Entry<Method, Object> it : getFilterAfterHandlers().entrySet()) {
                it.getKey().invoke(it.getValue(), request, response);
            }


            byte[] bytes = finalReturnProcessing(response, rtn);
            response.body(bytes);
            log.info("method invoke result: {}", rtn);
        } catch (Exception e) {
            log.info("controller execute Error", e);
            Optional<MethodObjectSet> first = geExceptionHandlerAnnotaion(e.getClass());
            if (first.isPresent()) {
                try {
                    Object rtn = first.get().invoke(request, response);
                    response.body(finalReturnProcessing(response, rtn));
                } catch (Exception se) {
                    response.status(HttpStatus.INTERNAL_SERVER_ERROR);
                    log.info("exceptionHandler Exception", se);
                }
            }
        }
    }


    private byte[] finalReturnProcessing(Response response, Object rtn) throws IOException {
        if (null != rtn && String.class.isAssignableFrom(rtn.getClass())) {
            return ((String) rtn).getBytes(StandardCharsets.UTF_8);
        } else if (null != rtn && View.class.isAssignableFrom(rtn.getClass())) {
            final TemplateEngine templateEngine = new TemplateEngine();
            Context context = new Context();
            View rtnView = (View) rtn;
            context.setVariables(rtnView);
            response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
            String viewString = Resources.toString(Resources.getResource(rtnView.getView()), Charsets.UTF_8);
            final String result = templateEngine.process(viewString, context);
            return result.getBytes(StandardCharsets.UTF_8);
        } else if (null != rtn) {
            response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            return mapper.writeValueAsString(rtn).getBytes(StandardCharsets.UTF_8);
        } else {
            return new byte[0];
        }
    }
}
