package com.simple.boot.web.thymeleaf;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.simple.boot.anno.Config;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.controller.returns.View;
import com.simple.boot.web.http.HttpHeaderNames;
import com.simple.boot.web.http.HttpHeaderValues;
import com.simple.boot.web.view.ViewResolver;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Slf4j
@Config
public class ThymeleafViewResolver implements ViewResolver {
    @Override
    public String process(Request request, Response response, View view) {
        final TemplateEngine templateEngine = new TemplateEngine();
        Context context = new Context();
        context.setVariables(view);
        response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
        String viewString = "";
        try {
            viewString = Resources.toString(Resources.getResource(view.getView()), Charsets.UTF_8);
        } catch (IOException e) {
            log.error("ThymeleafViewResolver error", e);
        }
        return templateEngine.process(viewString, context);
    }
}
