package com.simple.boot.web;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.starter.Starter;
import com.simple.boot.web.server.WebServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Config(order = -1_010_000)
public class WebStarter extends Starter {


    private final WebServer webServer;

    @Injection
    public WebStarter(WebServer webServer) throws Exception {
        this.webServer = webServer;
        init();
    }

    public void init() throws Exception {
        webServer.start();
    }
}
