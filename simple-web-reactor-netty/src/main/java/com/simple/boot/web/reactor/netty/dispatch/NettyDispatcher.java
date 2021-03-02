package com.simple.boot.web.reactor.netty.dispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.simple.boot.anno.Controller;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.web.anno.ExceptionHandler;
import com.simple.boot.web.controller.anno.*;
import com.simple.boot.web.controller.returns.View;
import com.simple.boot.web.dispatch.Dispatcher;
import com.simple.boot.web.reactor.netty.communication.NettyRequest;
import com.simple.boot.web.reactor.netty.communication.NettyResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class NettyDispatcher extends Dispatcher {

    private final HttpServerRoutes routes;
    private final ObjectMapper mapper;
    private final SimstanceManager simstanceManager;

    public NettyDispatcher(SimstanceManager simstanceManager, HttpServerRoutes routes) {
        super(simstanceManager);
        this.simstanceManager = simstanceManager;
        this.routes = routes;
        mapper = new ObjectMapper();
    }

    @Override
    public void mapping() {
        Stream<Map.Entry<Class, Object>> controllers = simstanceManager.getSims().entrySet().stream().filter(it -> it.getKey().isAnnotationPresent(Controller.class));
        controllers.forEach(controllerEntry -> {
            Class controllerClass = controllerEntry.getKey();
            Object controller = controllerEntry.getValue();
            Stream.of(controllerClass.getDeclaredMethods()).forEach(method -> {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    routes.get(method.getAnnotation(GetMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    routes.post(method.getAnnotation(PostMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
                } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                    routes.delete(method.getAnnotation(DeleteMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
                } else if (method.isAnnotationPresent(PutMapping.class)) {
                    routes.put(method.getAnnotation(PutMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
                } else if (method.isAnnotationPresent(OptionsMapping.class)) {
                    routes.options(method.getAnnotation(OptionsMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
                }
            });
        });
    }


    public Publisher<Void> mappingDetail(Object controller, Method method, HttpServerRequest request, HttpServerResponse response) {
//        NettyRequest nettyRequest = new NettyRequest(request);
//        NettyResponse nettyResponse = new NettyResponse(response);

        ///////////reacte

        return response.sendByteArray(
            request.receive().asByteArray().map(s -> {
                NettyRequest nettyRequest = new NettyRequest(request);
                NettyResponse nettyResponse = new NettyResponse(response);
                nettyRequest.setBody(new String(s, StandardCharsets.UTF_8));
                try {

                    //before
                    for (Map.Entry<Method, Object> it : getFilterBeforeHandlers().entrySet()) {
                        it.getKey().invoke(it.getValue(), nettyRequest, nettyResponse);
                    }
                    Object rtn = method.invoke(controller, nettyRequest, nettyResponse);
                    //after
                    for (Map.Entry<Method, Object> it : getFilterAfterHandlers().entrySet()) {
                        it.getKey().invoke(it.getValue(), nettyRequest, nettyResponse);
                    }
                    return finalReturnProcessing(response, rtn);
                } catch (Throwable e) {
                    Optional<Map.Entry<Method, Object>> first = getExceptionHandlers().entrySet().stream().filter(it -> it.getKey().getAnnotation(ExceptionHandler.class).value().isAssignableFrom(e.getClass())).findFirst();
                    if (first.isPresent()) {
                        try {
                            Map.Entry<Method, Object> methodObjectEntry = first.get();
                            Object rtn = methodObjectEntry.getKey().invoke(methodObjectEntry.getValue(), nettyRequest, nettyResponse);
                            return finalReturnProcessing(response, rtn);
                        } catch (Exception se) {
                            log.error("exceptionHandler Exception", se);
                        }
                    }
                    //exception
                    log.error("exception dispatch", e);
                    response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                    return null;
                }
            })
        );
    }

    private byte[] finalReturnProcessing(HttpServerResponse response, Object rtn) throws IOException {
        if (null != rtn && String.class.isAssignableFrom(rtn.getClass())) {
            return ((String) rtn).getBytes(StandardCharsets.UTF_8);

        } else if (null != rtn && View.class.isAssignableFrom(rtn.getClass())) {
            final TemplateEngine templateEngine = new TemplateEngine();
            Context context = new Context();
            View rtnView = (View) rtn;
            context.setVariables(rtnView);
            response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
            String viewString = Resources.toString(Resources.getResource(rtnView.getView()), Charsets.UTF_8);
            final String result = templateEngine.process(viewString, context);
            return result.getBytes(StandardCharsets.UTF_8);

//        } else if (null != rtn && Publisher.class.isAssignableFrom(rtn.getClass())) {
////             https://awesomeopensource.com/project/reactor/reactor-netty
//            ByteBufFlux.fromString(Flux.just("Hello"));
//            return response.sendString((Publisher<? extends String>) rtn); //return response.sendObject(rtn);
        } else if (null != rtn) {
            response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            return mapper.writeValueAsString(rtn).getBytes(StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }


//    private boolean isRunTlogAdviceToRun(Throwable e, Tlo tlo) {
//        boolean sw = false;
//        Map<String, Object> apioperationAdvices = applicationContext.getBeansWithAnnotation(TlogAdvice.class);
//        for (Object ot : apioperationAdvices.values()) {
//            Map<Method, Class<? extends Throwable>> unsortMap = new LinkedHashMap<>();
//            for (Method mt : ReflectionUtils.getAllDeclaredMethods(ot.getClass())) {
//                TlogExceptionHandler exceptionHandler = mt.getAnnotation(TlogExceptionHandler.class);
//                if (null != exceptionHandler && null != exceptionHandler.value() && Arrays.asList(exceptionHandler.value()).stream().filter(it -> it.isAssignableFrom(e.getClass())).findFirst().isPresent()) {
//                    unsortMap.put(mt, exceptionHandler.value());
//                }
//            }
//
//
//            // value 내림차순으로 정렬하고, value가 같으면 key 오름차순으로 정렬
//            /*  여기에는 3가지 규칙이 있다.
//                기준이 되는 데이터가 비교하는 데이터보다 큰 경우 양수를 리턴
//                기준이 되는 데이터가 비교하는 데이터와 같은 경우 0을 리턴
//                기준이 되는 데이터가 비교하는 데이터보다 작은 경우 음수를 리턴
//             */
//            List<Map.Entry<Method, Class<? extends Throwable>>> list = new LinkedList<>(unsortMap.entrySet());
//            Optional<Map.Entry<Method, Class<? extends Throwable>>> first = list.stream().sorted((a, b) -> superClassSize(b.getValue()) - superClassSize(a.getValue())).findFirst();
//            if (first.isPresent()) {
//                Map.Entry<Method, Class<? extends Throwable>> f = first.get();
//                ReflectionUtils.invokeMethod(f.getKey(), ot, e, tlo);
//                sw = true;
//            }
//        }
//        return sw;
//    }
}

