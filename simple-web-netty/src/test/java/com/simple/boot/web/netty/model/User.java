package com.simple.boot.web.netty.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    public String name;
    public Integer age;

    @Builder
    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
