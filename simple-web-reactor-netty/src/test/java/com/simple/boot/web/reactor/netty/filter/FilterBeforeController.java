package com.simple.boot.web.reactor.netty.filter;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.FilterBeforeHandler;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class FilterBeforeController {

    @FilterBeforeHandler(order = 1)
    public void before1(Request request, Response response) {
        log.info("filterBefore1 {}, {}", request, response);
    }
    @FilterBeforeHandler(order = 2)
    public void before2(Request request, Response response) {
        log.info("filterBefore2 {}, {}", request, response);
    }

}
