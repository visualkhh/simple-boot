package com.simple.boot.web;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.starter.Starter;
import lombok.extern.slf4j.Slf4j;

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

    }
}
