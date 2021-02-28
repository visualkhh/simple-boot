package com.simple.boot.config;

public interface ConfigLoader {

    public void load();

    public <T> T get(String key, Class<T> klass);
}
