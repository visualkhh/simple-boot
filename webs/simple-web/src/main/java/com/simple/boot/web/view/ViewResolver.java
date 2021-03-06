package com.simple.boot.web.view;

import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.returns.View;

public interface ViewResolver {

    public final String REDIRECT_PREFIX = "redirect:";

    public String process(Request request, Response response, View view);
}
