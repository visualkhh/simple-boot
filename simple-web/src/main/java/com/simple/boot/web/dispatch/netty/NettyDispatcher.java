package com.simple.boot.web.dispatch.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.simple.boot.anno.Controller;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.web.anno.*;
import com.simple.boot.web.communication.netty.NettyRequest;
import com.simple.boot.web.communication.netty.NettyResponse;
import com.simple.boot.web.dispatch.Dispatcher;
import com.simple.boot.web.controller.returns.View;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class NettyDispatcher implements Dispatcher {

    private final HttpServerRoutes routes;
    private final ObjectMapper mapper;

    public NettyDispatcher(HttpServerRoutes routes) {
        this.routes = routes;
        mapper = new ObjectMapper();
    }

    @Override
    public void mapping() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Stream<Map.Entry<Class, Object>> controllers = SimstanceManager.getInstance().getSims().entrySet().stream().filter(it -> it.getKey().isAnnotationPresent(Controller.class));
        controllers.forEach(controllerEntry -> {
            Class controllerClass = controllerEntry.getKey();
            Object controller = controllerEntry.getValue();
            Reflections reflections = new Reflections(controllerClass.getName(), new MethodAnnotationsScanner());

            reflections.getMethodsAnnotatedWith(GetMapping.class).stream().forEach(method -> {
                routes.get(method.getAnnotation(GetMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
            });
            reflections.getMethodsAnnotatedWith(PostMapping.class).stream().forEach(method -> {
                routes.post(method.getAnnotation(PostMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
            });
            reflections.getMethodsAnnotatedWith(DeleteMapping.class).stream().forEach(method -> {
                routes.delete(method.getAnnotation(DeleteMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
            });
            reflections.getMethodsAnnotatedWith(PutMapping.class).stream().forEach(method -> {
                routes.put(method.getAnnotation(PutMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
            });
            reflections.getMethodsAnnotatedWith(OptionsMapping.class).stream().forEach(method -> {
                routes.options(method.getAnnotation(OptionsMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
            });
        });
    }


    public Publisher<Void> mappingDetail(Object controller, Method method, HttpServerRequest request, HttpServerResponse response) {
        try {
            NettyRequest nettyRequest = new NettyRequest(request);
            NettyResponse nettyResponse = new NettyResponse(response);
            Object rtn = method.invoke(controller, nettyRequest, nettyResponse);
            if(null != rtn && String.class.isAssignableFrom(rtn.getClass())) {
                return response.sendString(Mono.just((String)rtn));
            } else if(null != rtn && View.class.isAssignableFrom(rtn.getClass())) {
                final TemplateEngine templateEngine = new TemplateEngine();
                Context context = new Context();
                View rtnView = (View)rtn;

                context.setVariables(rtnView);

                response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
                String viewString = Resources.toString(Resources.getResource(rtnView.getView()), Charsets.UTF_8);
                final String result = templateEngine.process(viewString, context);
                return response.sendString(Mono.just(result));
            } else if(null != rtn) {
                response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
                return response.sendString(Mono.just(mapper.writeValueAsString(rtn)));
            } else {
                return response.send();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
