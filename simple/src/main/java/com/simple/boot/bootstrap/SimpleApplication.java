package com.simple.boot.bootstrap;

import com.simple.boot.config.ConfigLoader;
import com.simple.boot.config.YamlConfigLoader;
import com.simple.boot.simstance.SimstanceManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
public class SimpleApplication implements SimpleBoot {

    private ConfigLoader configLoader;
    private SimstanceManager simstanceManager;

    @Override
    public void run(Class start) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.debug("start boot {}", start);
        configLoader = new YamlConfigLoader();
        configLoader.load();

        Map<Class, Object> defined = new LinkedHashMap<>();
        defined.put(ConfigLoader.class, configLoader);

        simstanceManager = SimstanceManager.getInstance(defined, start);


    }

    @Override
    public <T> T getSim(Class<T> klass) {
        return (T)simstanceManager.getSims().get(klass);
    }
}
