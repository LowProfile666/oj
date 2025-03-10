package com.zsm.oj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zsm.oj.judge.codesandbox.CodeSandBox;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxRequest;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxResponse;
import org.springframework.web.client.RestTemplate;

/**
 * 远程调用的代码沙箱实现
 * author: ZSM
 * time: 2025-03-01 16:34
 */
public class RemoteSandBox implements CodeSandBox {
    private final String REMOTE_URL = "http://localhost:8102/execute";

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
