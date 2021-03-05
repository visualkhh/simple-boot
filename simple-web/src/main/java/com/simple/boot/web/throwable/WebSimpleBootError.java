package com.simple.boot.web.throwable;

import com.simple.boot.throwable.SimpleBootException;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;

public class WebSimpleBootError extends SimpleBootException {
    public Request request;
    public Response response;
    public WebSimpleBootError() {
    }
    public WebSimpleBootError(Request request, Response response) {
        this.request = request;
        this.response = response;
    }
    public WebSimpleBootError(String msg, Request request, Response response) {
        super(msg);
        this.request = request;
        this.response = response;
    }

    public WebSimpleBootError(Throwable cause) {
        super(cause);
    }
}
