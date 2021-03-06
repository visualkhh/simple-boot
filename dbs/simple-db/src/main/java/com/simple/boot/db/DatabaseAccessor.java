package com.simple.boot.db;

import java.io.Serializable;
import java.util.List;

public interface DatabaseAccessor {
    Serializable save(Object data);

    <T> T find(Class<T> data, Serializable key);

    <T> List<T> resultList(Class<T> data);

}
