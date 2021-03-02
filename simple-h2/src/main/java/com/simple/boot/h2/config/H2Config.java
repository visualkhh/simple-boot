package com.simple.boot.h2.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class H2Config {
    public static final String prefix = "h2";
    Integer port;
}
