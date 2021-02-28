package com.example;

import com.simple.boot.web.bootstrap.SimpleWebApplication;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ExampleApplication {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new SimpleWebApplication().run(ExampleApplication.class);
    }
}
