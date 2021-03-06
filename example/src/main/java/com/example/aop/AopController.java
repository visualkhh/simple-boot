package com.example.aop;

import com.simple.boot.anno.Controller;
import com.simple.boot.aop.Aop;
import com.simple.boot.aop.anno.AopAfter;
import com.simple.boot.aop.anno.AopBefore;
import com.simple.boot.aop.anno.AopException;
import com.simple.boot.aop.anno.AopFinally;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AopController {

    @AopAfter(regex = "public .*.TodoController.admins(.*)")
    public void afterAop(Aop aop) {
        log.info("afterAop {}", aop);
    }

    @AopBefore(regex = "public .*.TodoController.admins(.*)")
    public void beforeAop(Aop aop) {
        log.info("beforeAop {}", aop);
    }

    @AopException(regex = "public .*.TodoController.admins(.*)")
    public void exception(Aop aop) {
        log.info("exception {}", aop);
    }
    @AopFinally(regex = "public .*.TodoController.admins(.*)")
    public void aopFinal(Aop aop) {
        log.info("aopFinal {}", aop);
    }
}
