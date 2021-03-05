package com.example.exception;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.ExceptionHandler;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.http.HttpStatus;
import com.simple.boot.web.throwable.WebNoSurchHttpMethodError;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Controller
@Slf4j
public class ExceptionController {

    @ExceptionHandler(WebNoSurchHttpMethodError.class)
    public void noSurch(Request request, Response response) {
        log.info("WebNoSurchHttpMethodError {}, {}", request, response);
        response.status(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public void exception(Request request, Response response) {
        response.status(HttpStatus.INTERNAL_SERVER_ERROR);
        log.info("exception {}, {}", request, response);
    }
}
