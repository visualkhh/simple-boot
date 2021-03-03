package com.simple.boot.web.netty.dispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.anno.*;
import com.simple.boot.web.dispatch.Dispatcher;
import com.simple.boot.web.http.HttpMethod;
import com.simple.boot.web.model.MethodObjectSet;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Config
public class NettyDispatcher extends Dispatcher {

    @Injection
    public NettyDispatcher(SimstanceManager simstanceManager) {
        super(simstanceManager);
    }

//    @Override
//    public void executeMapping(Request request, Response response) {
//        Optional<MethodObjectSet> method = null;
//        if (HttpMethod.GET == request.method()) {
//            method = getControllerMappingAnnotaion(GetMapping.class, (a) -> a.value().equals(request.path()));
//        } else if(HttpMethod.POST == request.method()) {
//            method = getControllerMappingAnnotaion(PostMapping.class, (a) -> a.value().equals(request.path()));
//        } else if(HttpMethod.POST == request.method()) {
//            method = getControllerMappingAnnotaion(DeleteMapping.class, (a) -> a.value().equals(request.path()));
//        } else if(HttpMethod.POST == request.method()) {
//            method = getControllerMappingAnnotaion(PutMapping.class, (a) -> a.value().equals(request.path()));
//        } else if(HttpMethod.POST == request.method()) {
//            method = getControllerMappingAnnotaion(OptionsMapping.class, (a) -> a.value().equals(request.path()));
//        } else {
//            method = Optional.empty();
//        }
//        if(method.isPresent()) {
//            try {
//                String rtn = (String)method.get().invoke(request, response);
//                response.body(rtn.getBytes(StandardCharsets.UTF_8));
//                log.info("method invoke result: {}", rtn);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public void mapping() {
////        Stream<Map.Entry<Class, Object>> controllers = simstanceManager.getSims().entrySet().stream().filter(it -> it.getKey().isAnnotationPresent(Controller.class));
//        getControllers().forEach((controllerClass, controller) -> {
//            Stream.of(controllerClass.getDeclaredMethods()).forEach(method -> {
//                GetMapping annotation = method.getAnnotation(GetMapping.class);
//                if (method.isAnnotationPresent(GetMapping.class)) {
//                    routes.get(method.getAnnotation(GetMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
//                } else if (method.isAnnotationPresent(PostMapping.class)) {
//                    routes.post(method.getAnnotation(PostMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
//                } else if (method.isAnnotationPresent(DeleteMapping.class)) {
//                    routes.delete(method.getAnnotation(DeleteMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
//                } else if (method.isAnnotationPresent(PutMapping.class)) {
//                    routes.put(method.getAnnotation(PutMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
//                } else if (method.isAnnotationPresent(OptionsMapping.class)) {
//                    routes.options(method.getAnnotation(OptionsMapping.class).value(), (request, response) -> mappingDetail(controller, method, request, response));
//                }
//            });
//        });
//    }


//    public Publisher<Void> mappingDetail(Object controller, Method method, HttpServerRequest request, HttpServerResponse response) {
////        NettyRequest nettyRequest = new NettyRequest(request);
////        NettyResponse nettyResponse = new NettyResponse(response);
//
//        ///////////reacte
//
//        return response.sendByteArray(
//            request.receive().asByteArray().map(s -> {
//                NettyRequest nettyRequest = new NettyRequest(request);
//                NettyResponse nettyResponse = new NettyResponse(response);
//                nettyRequest.setBody(new String(s, StandardCharsets.UTF_8));
//                try {
//
//                    //before
//                    for (Map.Entry<Method, Object> it : getFilterBeforeHandlers().entrySet()) {
//                        it.getKey().invoke(it.getValue(), nettyRequest, nettyResponse);
//                    }
//                    Object rtn = method.invoke(controller, nettyRequest, nettyResponse);
//                    //after
//                    for (Map.Entry<Method, Object> it : getFilterAfterHandlers().entrySet()) {
//                        it.getKey().invoke(it.getValue(), nettyRequest, nettyResponse);
//                    }
//                    return finalReturnProcessing(response, rtn);
//                } catch (Throwable e) {
//                    Optional<Map.Entry<Method, Object>> first = getExceptionHandlers().entrySet().stream().filter(it -> it.getKey().getAnnotation(ExceptionHandler.class).value().isAssignableFrom(e.getClass())).findFirst();
//                    if (first.isPresent()) {
//                        try {
//                            Map.Entry<Method, Object> methodObjectEntry = first.get();
//                            Object rtn = methodObjectEntry.getKey().invoke(methodObjectEntry.getValue(), nettyRequest, nettyResponse);
//                            return finalReturnProcessing(response, rtn);
//                        } catch (Exception se) {
//                            log.error("exceptionHandler Exception", se);
//                        }
//                    }
//                    //exception
//                    log.error("exception dispatch", e);
//                    response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
//                    return null;
//                }
//            })
//        );
//    }
//
//    private byte[] finalReturnProcessing(HttpServerResponse response, Object rtn) throws IOException {
//        if (null != rtn && String.class.isAssignableFrom(rtn.getClass())) {
//            return ((String) rtn).getBytes(StandardCharsets.UTF_8);
//
//        } else if (null != rtn && View.class.isAssignableFrom(rtn.getClass())) {
//            final TemplateEngine templateEngine = new TemplateEngine();
//            Context context = new Context();
//            View rtnView = (View) rtn;
//            context.setVariables(rtnView);
//            response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
//            String viewString = Resources.toString(Resources.getResource(rtnView.getView()), Charsets.UTF_8);
//            final String result = templateEngine.process(viewString, context);
//            return result.getBytes(StandardCharsets.UTF_8);
//
////        } else if (null != rtn && Publisher.class.isAssignableFrom(rtn.getClass())) {
//////             https://awesomeopensource.com/project/reactor/reactor-netty
////            ByteBufFlux.fromString(Flux.just("Hello"));
////            return response.sendString((Publisher<? extends String>) rtn); //return response.sendObject(rtn);
//        } else if (null != rtn) {
//            response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
//            return mapper.writeValueAsString(rtn).getBytes(StandardCharsets.UTF_8);
//        } else {
//            return null;
//        }
//    }

}

