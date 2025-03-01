package com.zsm.oj.judge.codesandbox.model;

import com.zsm.oj.judge.codesandbox.CodeSandBox;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱代理类
 * author: ZSM
 * time: 2025-03-01 19:34
 */
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {
    private final CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {
        log.info("代码沙箱代理记录日志，请求的参数:{}", codeSandBoxRequest);
        CodeSandBoxResponse response = codeSandBox.execute(codeSandBoxRequest);
        log.info("代码沙箱代理记录日志，响应的结果:{}", response);
        return response;
    }
}
