package com.simple.boot.throwable;

public class SimpleBootException extends Exception {

    public SimpleBootException() {
    }
    public SimpleBootException(String msg) {
        super(msg);
    }

    public SimpleBootException(Throwable cause) {
        super(cause);
    }
}
