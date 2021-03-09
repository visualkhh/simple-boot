package com.example;

import com.simple.boot.SimpleApplication;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ExampleApplication {

    public static void main(String[] args) {
        new SimpleApplication().run(ExampleApplication.class).join();
    }

}
