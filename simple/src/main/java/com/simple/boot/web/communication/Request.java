package com.simple.boot.web.communication;

import reactor.util.annotation.Nullable;

import java.util.Map;

public interface Request {
    public String param(String key);
    public Map<String, String> params();
    public String path();
}
