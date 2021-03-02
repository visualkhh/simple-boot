package com.simple.boot.web.communication;

public interface Response<T> {
    public void header(String header, String value);
    public void status(int header);
    public void body(T body);
}
