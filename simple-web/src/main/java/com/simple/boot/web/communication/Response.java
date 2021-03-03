package com.simple.boot.web.communication;

import com.simple.boot.web.http.HttpHeaderNames;
import com.simple.boot.web.http.HttpHeaderValues;
import com.simple.boot.web.http.HttpStatus;

public interface Response<T> {
    public void putHeader(HttpHeaderNames httpHeaderNames, HttpHeaderValues value);
    public void putHeader(String header, String value);
    public void status(HttpStatus httpStatus);
    public void status(int status);
    public void body(T body);
}
