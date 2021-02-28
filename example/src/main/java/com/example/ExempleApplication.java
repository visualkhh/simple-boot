package com.example;

import com.simple.boot.web.bootstrap.SimpleWebApplication;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ExempleApplication {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
      log.info("-------");
        new SimpleWebApplication().run(ExempleApplication.class);
    }
}
