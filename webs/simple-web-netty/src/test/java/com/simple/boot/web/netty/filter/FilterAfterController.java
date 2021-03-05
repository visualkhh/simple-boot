package com.simple.boot.web.netty.filter;

import com.simple.boot.anno.Controller;
import com.simple.boot.web.anno.FilterAfterHandler;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class FilterAfterController {

    @FilterAfterHandler
    public void after(Request request, Response response) {
        log.info("filterAfter {}, {}", request, response);
    }

}
