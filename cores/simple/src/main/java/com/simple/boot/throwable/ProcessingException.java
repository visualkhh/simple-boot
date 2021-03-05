package com.simple.boot.throwable;

public class ProcessingException extends SimpleBootException {

    public ProcessingException() {
    }
    
    public ProcessingException(Throwable e) {
        super(e);
    }
}
