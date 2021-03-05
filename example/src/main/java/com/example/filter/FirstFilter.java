package com.example.filter;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.FilterAfterHandler;
import com.simple.boot.web.anno.FilterBeforeHandler;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FirstFilter {

    @FilterAfterHandler
    public void after(Request request, Response r) {
         log.info("after");
    }
    @FilterBeforeHandler
    public void before(Request request, Response r) {
         log.info("before");
    }
}
