package com.zsm.ojbackendjudgeservice.codesandbox.impl;


import com.zsm.ojbackendjudgeservice.codesandbox.CodeSandBox;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxRequest;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxResponse;

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
