package com.simple.boot.hibernate.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HibernaterConfig {
    public static final String prefix = "hibernate";
    public Map<String, Object> property;
}
