package com.simple.boot.web.controller;

import com.simple.boot.anno.Injection;
import com.simple.boot.anno.Service;
import com.simple.boot.config.ConfigLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WowService {
    public WowService() {
        log.debug("wow aaaaaaaa hello Servic");
    }

}
