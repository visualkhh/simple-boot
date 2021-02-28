package com.project;

import com.simple.boot.web.bootstrap.SimpleWebApplication;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ProjectApplication {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
      log.info("-------");
        SimpleWebApplication simpleWebApplication = new SimpleWebApplication();
        simpleWebApplication.run(ProjectApplication.class);

    }
}
