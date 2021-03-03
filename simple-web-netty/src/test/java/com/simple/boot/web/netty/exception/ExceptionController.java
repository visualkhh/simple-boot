package com.simple.boot.web.netty.exception;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.ExceptionHandler;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.http.HttpStatus;
import com.simple.boot.web.throwable.NoSurchHttpMethodError;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Controller
@Slf4j
public class ExceptionController {
    @ExceptionHandler(NoSuchMethodException.class)
    public void exception(Request request, Response response) {
        log.info("exception {}, {}", request, response);
    }
    @ExceptionHandler(ClassCastException.class)
    public void ccexception(Request request, Response response) {
        log.info("ClassCastException {}, {}", request, response);
    }
    @ExceptionHandler(InvocationTargetException.class)
    public void ii(Request request, Response response) {
        log.info("InvocationTargetException {}, {}", request, response);
    }
    @ExceptionHandler(NoSurchHttpMethodError.class)
    public void nosurch(Request request, Response response) {
        response.status(HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(order = 100)
    public void ee(Request request, Response response) {
        log.info("eee {}, {}", request, response);
    }
}
