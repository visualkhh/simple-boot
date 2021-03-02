package com.simple.boot.web.netty;

import com.simple.boot.anno.Config;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.starter.Starter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Config(order = -1_010_000)
public class NettyStarter extends Starter {
    private SimstanceManager simstanceManager;
    private ConfigLoader configLoader;

    public NettyStarter(SimstanceManager simstanceManager, ConfigLoader configLoader) {
        this.simstanceManager = simstanceManager;
        this.simstanceManager = simstanceManager;
        this.configLoader = configLoader;
        this.configLoader = configLoader;
        init();
    }

    private void init() {

    }
}
