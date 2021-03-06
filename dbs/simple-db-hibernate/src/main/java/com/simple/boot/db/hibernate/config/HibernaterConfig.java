package com.simple.boot.db.hibernate.config;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.config.ConfigLoader;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Config
public class HibernaterConfig {

    public Map<String, Object> property;

    @Injection
    public HibernaterConfig(ConfigLoader configLoader) {
        configLoader.map("hibernate", this);
    }

}
