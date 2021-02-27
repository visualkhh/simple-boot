package com.project;

import com.simple.boot.web.bootstrap.SimpleWebApplication;

import java.lang.reflect.InvocationTargetException;

public class ProjectApplication {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        SimpleWebApplication simpleWebApplication = new SimpleWebApplication();
        simpleWebApplication.run(ProjectApplication.class);

    }
}
