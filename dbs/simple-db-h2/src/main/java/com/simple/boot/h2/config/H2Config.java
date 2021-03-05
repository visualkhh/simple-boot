package com.simple.boot.h2.config;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.config.ConfigLoader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Config
public class H2Config {
    
    Integer port;

    @Injection
    public H2Config(ConfigLoader configLoader) {
        configLoader.map("h2", this);
    }
}
