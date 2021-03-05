package com.simple.boot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Getter
@Setter
public class YamlConfigLoader implements ConfigLoader {

    private final String name = "simple-boot.yaml";
    private Map<String, Object> configs;
    private final ModelMapper modelMapper;

    public YamlConfigLoader() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        yaml = new Yaml();
//        inputStream = this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("simple-boot.yaml");
    }

    @Override
    public void load() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(name);
        configs =  Collections.unmodifiableMap(yaml.load(inputStream));
    }

    @Override
    public <T> T get(String key, Class<T> klass) {
        return modelMapper.map(configs.get(key), klass);
    }

    @Override
    public void map(String key, Object destination) {
        modelMapper.map(configs.get(key), destination);
    }

//
//    @Override
//    public <T> T load(Class<T> loadClass) {
//        Yaml yaml = new Yaml(new Constructor(loadClass));
//        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(name);
//        return yaml.load(inputStream);
//    }


}
