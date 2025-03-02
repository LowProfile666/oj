package com.zsm.ojsandbox.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ZSM
 * time: 2025-03-02 10:02
 */
@RestController
public class IndexController {
    @RequestMapping("health")
    public String health() {
        return "ok";
    }
}
