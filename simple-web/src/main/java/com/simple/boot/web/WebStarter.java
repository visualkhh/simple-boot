package com.simple.boot.web;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.starter.Starter;
import com.simple.boot.web.config.WebConfig;
import com.simple.boot.web.connection.netty.NettyConnection;
import com.simple.boot.web.dispatch.netty.NettyDispatcher;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

@Slf4j
@Config(order = -1_010_000)
public class WebStarter extends Starter {

    private final SimstanceManager simstanceManager;
    private final ConfigLoader configLoader;

    @Injection
    public WebStarter(SimstanceManager simstanceManager, ConfigLoader configLoader) {
        this.simstanceManager = simstanceManager;
        this.configLoader = configLoader;
        init();
    }

    public void init() {
        WebConfig config = configLoader.get(WebConfig.prefix, WebConfig.class);
        HttpServer httpServer = new NettyConnection().create().host(config.getHost()).port(config.getPort());
        DisposableServer server = httpServer.route(routes -> {
            try {
                new NettyDispatcher(simstanceManager, routes).mapping();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).wiretap(true).bindNow();
        server.onDispose().block();
    }
}
