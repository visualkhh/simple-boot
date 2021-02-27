package com.simple.boot.bootstrap;

import com.simple.boot.config.YamlConfigLoader;
import com.simple.boot.simstance.SimstanceManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@Getter
@Setter
public class SimpleApplication implements SimpleBoot {

    private YamlConfigLoader yamlConfigLoader;
    private SimstanceManager simstanceManager;

    @Override
    public void run(Class start) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.debug("start boot {}", start);
        yamlConfigLoader = new YamlConfigLoader();
        yamlConfigLoader.load();

        simstanceManager = SimstanceManager.getInstance(start);


    }
}
