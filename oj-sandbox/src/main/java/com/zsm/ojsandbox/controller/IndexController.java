package com.zsm.ojsandbox.controller;

import com.zsm.ojsandbox.model.CodeSandBoxRequest;
import com.zsm.ojsandbox.model.CodeSandBoxResponse;
import com.zsm.ojsandbox.service.CodeSandBox;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ZSM
 * time: 2025-03-02 10:02
 */
@RestController
public class IndexController {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER_NAME = "auth";
    private static final String AUTH_REQUEST_HEADER_VALUE = "secretKey";

    @Resource
    private CodeSandBox codeSandBox;

    @RequestMapping("health")
    public String health() {
        return "ok";
    }

    @PostMapping("execute")
    public CodeSandBoxResponse execute(@RequestBody CodeSandBoxRequest codeSandBoxRequest, HttpServletRequest request, HttpServletResponse response) {
        if (codeSandBoxRequest == null) {
            throw new RuntimeException("请求参数为空");
        }

        String requestHeader = request.getHeader(AUTH_REQUEST_HEADER_NAME);
        if (!AUTH_REQUEST_HEADER_VALUE.equals(requestHeader)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        CodeSandBoxRequest codeSanBoxRequest = new CodeSandBoxRequest();

        codeSanBoxRequest.setCode(codeSandBoxRequest.getCode());
        codeSanBoxRequest.setInputs(codeSandBoxRequest.getInputs());
        codeSanBoxRequest.setLanguage(codeSandBoxRequest.getLanguage());

        return codeSandBox.execute(codeSanBoxRequest);
    }
}
