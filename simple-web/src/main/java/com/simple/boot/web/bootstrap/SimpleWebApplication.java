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

@Slf4j
public class SimpleWebApplication extends SimpleApplication implements SimpleBoot {
    @Override
    public void run(Class start) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super.run(start);
        WebConfig webConfig = this.getYamlConfigLoader().load(WebConfig.class);
        HttpServer httpServer = new NettyConnection().create().host(webConfig.getWeb().getHost()).port(webConfig.getWeb().getPort());
        DisposableServer server = httpServer.route(routes -> {
            try {
                new NettyDispatcher(routes).mapping();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).wiretap(true).bindNow();
        server.onDispose().block();
    }
}
