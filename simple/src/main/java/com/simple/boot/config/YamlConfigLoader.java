package com.simple.boot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Getter
@Setter
public class YamlConfigLoader implements ConfigLoader {

    private Map<String, Object> configs;

    @Override
    public void load() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("simple-boot.yaml");
        configs =  Collections.unmodifiableMap(yaml.load(inputStream));
        log.debug(configs.toString());
    }


}
