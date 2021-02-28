package com.simple.boot.config;

public interface ConfigLoader {

    public void load();

    public <T> T load(Class<T> loadClass);
}
