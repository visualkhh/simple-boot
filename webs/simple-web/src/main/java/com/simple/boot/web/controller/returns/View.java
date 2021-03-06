package com.simple.boot.web.controller.returns;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class View extends HashMap<String, Object> {

    String view;

    public View(String view){
        this.view = view;
    }
}
