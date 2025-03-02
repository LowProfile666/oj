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
@Slf4j
public class JavaNativeSandBox implements CodeSandBox {
    // 用户代码的根路径的名字
    private final String USER_CODE_ROOT_DIR_NAME = "uercode";
    // 用户代码文件的名字
    private final String USER_CODE_FILE_NAME = "Main.java";
    // 超时时间
    private final long TTL = 5000;
    // 黑名单
    private static final List<String> BALCK_LIST = Arrays.asList("Files", "exec");
    // 字典数
    private static final WordTree WORD_TREE = new WordTree();

    static {
        WORD_TREE.addWords(BALCK_LIST);
    }


    public static void main(String[] args) {
        JavaNativeSandBox javaNativeSandBox = new JavaNativeSandBox();
        CodeSandBoxRequest request = new CodeSandBoxRequest();

        String code = ResourceUtil.readStr("test/Main.java", StandardCharsets.UTF_8);

        request.setCode(code);
        request.setInputs(Arrays.asList("1 2", "3 4"));
        request.setLanguage("java");

        javaNativeSandBox.execute(request);
    }

    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {
//        System.setSecurityManager(new DefaultSecurityManager());

        String code = codeSandBoxRequest.getCode();
        List<String> inputs = codeSandBoxRequest.getInputs();
        String language = codeSandBoxRequest.getLanguage();

        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null) {
            log.warn("发现敏感词: {}", foundWord);
            return null;
        }

        CodeSandBoxResponse response = new CodeSandBoxResponse();

        // 获取到项目中存放所有用户代码的根路径
        String userCodeRootDirPath = System.getProperty("user.dir") + File.separator + USER_CODE_ROOT_DIR_NAME;
        // 不存在则创建该目录
        if (!FileUtil.exist(userCodeRootDirPath)) {
            FileUtil.mkdir(userCodeRootDirPath);
        }

        // 设置单个用户存放代码的目录路径
        String userCodeDirName = userCodeRootDirPath + File.separator + UUID.randomUUID();
        String userCodeFilePath = userCodeDirName + File.separator + USER_CODE_FILE_NAME;
        // 将代码写到文件中去
        File userCodeFile = FileUtil.writeString(code, userCodeFilePath, StandardCharsets.UTF_8);

        try {
            ProcessExecResult processExecResult = new ProcessExecResult();

            // 设置 javac 编译命令
            String javacCmd = String.format("javac -encoding %s %s", "utf-8", userCodeFile.getAbsolutePath());
            Runtime runtime = Runtime.getRuntime();

            // 使用 Process 进程类，获取编译的进程类
            Process javacProcess = runtime.exec(javacCmd);
            processExecResult = ProcessUtil.execCmdAndGetMessage(javacProcess, "编译");
            System.out.println(processExecResult.getMessage());

            JudgeInfo judgeInfo = new JudgeInfo();

            List<String> outputLit = new ArrayList<>();
            long maxTime = 0L;
            // 循环每一组输入
            for (String args : inputs) {
                // 设置 java 运行命令
                String javaCmd = String.format("java -Xmx4096m %s %s", userCodeFile.getAbsolutePath(), args);
                // 创建运行进程
                Process javaProcess = runtime.exec(javaCmd);

                new Thread(() -> {
                    try {
                        Thread.sleep(TTL);
                        System.out.println("执行超时，中断进程");
                        if (javaProcess.isAlive()) {
                            javaProcess.destroy();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                processExecResult = ProcessUtil.execCmdAndGetMessage(javaProcess, "运行");
                if (processExecResult.getExitCode() != 0) {
                    response.setMessage(processExecResult.getMessage());
                    judgeInfo.setMessage(processExecResult.getMessage());
                    response.setStatus(1);
                    return response;
                }
                maxTime = Math.max(processExecResult.getTime(), maxTime);
                outputLit.add(processExecResult.getMessage());
            }

            if (outputLit.size() != inputs.size()) {
                response.setStatus(1);
                return response;
            }

            // todo 暂时用最大时间作为超时的判断，以后可以改成每个用例对应一个时间
            judgeInfo.setTime(maxTime);
            judgeInfo.setMemory(1L);

            response.setJudgeInfo(judgeInfo);
            response.setOutputs(outputLit);
            response.setStatus(0);

            if (FileUtil.exist(userCodeDirName)) {
                FileUtil.del(userCodeDirName);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return getErrorResponse(e);
        }

        return response;
    }

    private CodeSandBoxResponse getErrorResponse(Exception e) {
        CodeSandBoxResponse response = new CodeSandBoxResponse();
        response.setMessage(e.getMessage());
        response.setOutputs(new ArrayList<>());
        response.setJudgeInfo(new JudgeInfo());
        response.setStatus(2);
        return response;
    }
}
