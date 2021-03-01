package com.simple.boot.web.bootstrap;

import com.simple.boot.bootstrap.SimpleApplication;
import com.simple.boot.bootstrap.SimpleBoot;
import com.simple.boot.web.config.WebConfig;
import com.simple.boot.web.connection.netty.NettyConnection;
import com.simple.boot.web.dispatch.netty.NettyDispatcher;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
public class SimpleWebApplication extends SimpleApplication implements SimpleBoot {
    @Override
    public void run(Class start) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super.run(start);
        WebConfig web = this.getConfigLoader().get("web", WebConfig.class);
        HttpServer httpServer = new NettyConnection().create().host(web.getHost()).port(web.getPort());
        DisposableServer server = httpServer.route(routes -> {
            try {
                new NettyDispatcher(this.getSimstanceManager(), routes).mapping();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).wiretap(true).bindNow();
        server.onDispose().block();
    }
}
