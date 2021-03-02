package com.simple.boot.web.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebConfig {
    public static final String prefix = "web";
    String host;
    Integer port;
}
