package com.zsm.ojsandbox.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.zsm.ojsandbox.model.CodeSandBoxRequest;
import com.zsm.ojsandbox.model.CodeSandBoxResponse;
import com.zsm.ojsandbox.model.JudgeInfo;
import com.zsm.ojsandbox.model.ProcessExecResult;
import com.zsm.ojsandbox.sercurity.DefaultSecurityManager;
import com.zsm.ojsandbox.service.CodeSandBox;
import com.zsm.ojsandbox.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * author: ZSM
 * time: 2025-03-02 13:25
 */
@Service
public class JavaNativeSandBox extends JavaCodeSandBoxTemplate {
    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {
        return super.execute(codeSandBoxRequest);
    }
}

