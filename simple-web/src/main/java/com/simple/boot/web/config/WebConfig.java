package com.simple.boot.web.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebConfig {
    @Getter
    @Setter
    public static class Detail {
        String host;
        Integer port;

    }

    public Detail web;
}
