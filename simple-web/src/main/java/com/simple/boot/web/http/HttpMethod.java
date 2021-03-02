package com.simple.boot.web.http;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    public static List<String> getMethods() {
        return Arrays.asList(HttpMethod.values()).stream().map(it -> it.name()).collect(Collectors.toList());
    }
    public static String getRandomgeMethod() {
        List<String> datas = getMethods();
        return datas.get(new Random().nextInt(datas.size()));
    }
}
