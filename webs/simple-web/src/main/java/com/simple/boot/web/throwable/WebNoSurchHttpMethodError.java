package com.simple.boot.web.throwable;

import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;

public class WebNoSurchHttpMethodError extends WebSimpleBootError {


    public WebNoSurchHttpMethodError() {
    }

    public WebNoSurchHttpMethodError(Request request, Response response) {
        super(request, response);
    }

    public WebNoSurchHttpMethodError(String msg, Request request, Response response) {
        super(msg, request, response);
    }

    public WebNoSurchHttpMethodError(Throwable cause) {
        super(cause);
    }
}
