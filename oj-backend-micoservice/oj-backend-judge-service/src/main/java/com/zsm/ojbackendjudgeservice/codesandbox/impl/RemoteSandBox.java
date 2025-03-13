package com.zsm.ojbackendjudgeservice.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zsm.ojbackendjudgeservice.codesandbox.CodeSandBox;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxRequest;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxResponse;


/**
 * 远程调用的代码沙箱实现
 * author: ZSM
 * time: 2025-03-01 16:34
 */
public class RemoteSandBox implements CodeSandBox {
    private final String REMOTE_URL = "http://localhost:8105/execute";

    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {
        String requestBody = JSONUtil.toJsonStr(codeSandBoxRequest);
        String response = HttpUtil.createPost(REMOTE_URL)
                .header("auth","secretKey")
                .body(requestBody)
                .execute()
                .body();

        return JSONUtil.toBean(response, CodeSandBoxResponse.class);
    }
}
