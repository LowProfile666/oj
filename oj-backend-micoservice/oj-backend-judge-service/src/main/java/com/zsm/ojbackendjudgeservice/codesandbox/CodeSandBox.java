package com.zsm.ojbackendjudgeservice.codesandbox;


import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxRequest;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxResponse;

/**
 * 代码沙箱接口
 * author: ZSM
 * time: 2025-03-01 16:28
 */
public interface CodeSandBox {
    /**
     * 执行代码的方法
     * @param codeSandBoxRequest
     * @return
     */
    CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest);
}
