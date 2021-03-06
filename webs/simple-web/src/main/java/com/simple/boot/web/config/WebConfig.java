package com.simple.boot.web.config;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.config.ConfigLoader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Config
public class WebConfig {

    String host;
    Integer port;
    Boolean ssl;

    @Injection
    public WebConfig(ConfigLoader configLoader) {
        configLoader.map("web", this);
    }
}
