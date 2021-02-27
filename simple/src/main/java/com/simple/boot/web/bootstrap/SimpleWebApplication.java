package com.simple.boot.web.bootstrap;

import com.simple.boot.anno.Controller;
import com.simple.boot.bootstrap.SimpleApplication;
import com.simple.boot.bootstrap.SimpleBoot;
import com.simple.boot.web.anno.GetMapping;
import com.simple.boot.web.connection.NettyConnection;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SimpleWebApplication extends SimpleApplication implements SimpleBoot {
    @Override
    public void run(Class start) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super.run(start);

        HttpServer httpServer = new NettyConnection().create();

        Map<String, Object> configs = this.getYamlConfigLoader().getConfigs();
        Map<String, Object> webConfigs = (Map<String, Object>) configs.get("web");
        String host = (String) webConfigs.get("host");
        Integer port = (Integer) webConfigs.get("port");
        // TODO: dispatch
        DisposableServer server = httpServer.host(host).port(port.intValue()).route(routes -> {
            getSimstanceManager().getSims().entrySet().stream().filter(it -> it.getKey().isAnnotationPresent(Controller.class)).forEach(controllerEntry -> {
                Class controllerClass = controllerEntry.getKey();
                Object controller = controllerEntry.getValue();
                Reflections reflections = new Reflections(getSimstanceManager().getStartClass().getPackage().getName(), new MethodAnnotationsScanner());
                Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(GetMapping.class);
                methodsAnnotatedWith.stream().forEach(method -> {
                    log.debug("value {}", method);
                    routes.get(method.getAnnotation(GetMapping.class).value(), (request, response) -> {
                        try {
                            return (Publisher<Void>) method.invoke(controller, request, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return response.sendString(Mono.just("Error"));
                    });
                });
            });
        }).wiretap(true).bindNow();
        server.onDispose().block();
        //-----------
    }
}
