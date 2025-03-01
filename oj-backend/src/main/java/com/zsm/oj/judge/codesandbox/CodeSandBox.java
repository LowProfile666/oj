package com.zsm.oj.judge.codesandbox;

import com.zsm.oj.judge.codesandbox.model.CodeSandBoxRequest;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxResponse;

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
