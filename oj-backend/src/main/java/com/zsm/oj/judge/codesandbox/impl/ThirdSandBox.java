package com.zsm.oj.judge.codesandbox.impl;

import com.zsm.oj.judge.codesandbox.CodeSandBox;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxRequest;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxResponse;

/**
 * 第三方的代码沙箱实现
 * author: ZSM
 * time: 2025-03-01 16:34
 */
public class ThirdSandBox implements CodeSandBox {
    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {
        System.out.println("ThirdSandBox 执行");
        return CodeSandBoxResponse.builder().build();
    }
}
