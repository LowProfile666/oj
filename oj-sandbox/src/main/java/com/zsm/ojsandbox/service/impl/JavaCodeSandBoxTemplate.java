package com.zsm.ojsandbox.service.impl;

import cn.hutool.core.io.FileUtil;
import com.zsm.ojsandbox.model.CodeSandBoxRequest;
import com.zsm.ojsandbox.model.CodeSandBoxResponse;
import com.zsm.ojsandbox.model.JudgeInfo;
import com.zsm.ojsandbox.model.ProcessExecResult;
import com.zsm.ojsandbox.service.CodeSandBox;
import com.zsm.ojsandbox.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 模板类
 * author: ZSM
 * time: 2025-03-09 12:27
 */
@Slf4j
public abstract class JavaCodeSandBoxTemplate implements CodeSandBox {
    // 用户代码的根路径的名字
    public final String USER_CODE_ROOT_DIR_NAME = "usercode";
    // 用户代码文件的名字
    public final String USER_CODE_FILE_NAME = "Main.java";
    // 超时时间
    public final long TTL = 5000;


    /**
     * 1、保存用户提交的代码
     * @param codeSandBoxRequest
     * @return 保存的代码文件
     */
    public File saveCode(CodeSandBoxRequest codeSandBoxRequest) {
        String code = codeSandBoxRequest.getCode();

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
        return FileUtil.writeString(code, userCodeFilePath, StandardCharsets.UTF_8);
    }

    /**
     * 2、编译代码
     * @param userCodeFile
     * @return 编译后的结果
     */
    public ProcessExecResult compileCode(File userCodeFile) {
        ProcessExecResult processExecResult = new ProcessExecResult();

        // 设置 javac 编译命令
        String javacCmd = String.format("javac -encoding %s %s", "utf-8", userCodeFile.getAbsolutePath());
        Runtime runtime = Runtime.getRuntime();

        // 使用 Process 进程类，获取编译的进程类
        Process javacProcess = null;
        try {
            javacProcess = runtime.exec(javacCmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        processExecResult = ProcessUtil.execCmdAndGetMessage(javacProcess, "编译");
        if (processExecResult.getExitCode() != 0) {
            log.error("编译失败：{}", processExecResult.getMessage());
            throw new RuntimeException("编译失败");
        }
        return processExecResult;
    }

    /**
     * 3、运行代码
     * @param userCodeFile
     * @param inputList
     * @return 运行后的结果
     */
    public List<ProcessExecResult>  runCode(File userCodeFile, List<String> inputList) {
        List<ProcessExecResult> execResultList = new ArrayList<>();
        Runtime runtime = Runtime.getRuntime();
        // 循环每一组输入
        for (String args : inputList) {
            // 设置 java 运行命令
            String javaCmd = String.format("java -Xmx4096m %s %s", userCodeFile.getAbsolutePath(), args);
            // 创建运行进程
            Process[] javaProcess = {null};
            try {
                javaProcess[0] = runtime.exec(javaCmd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new Thread(() -> {
                try {
                    Thread.sleep(TTL);
                    if (javaProcess[0].isAlive()) {
                        log.error("执行超时，中断进程");
                        javaProcess[0].destroy();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            ProcessExecResult processExecResult = ProcessUtil.execCmdAndGetMessage(javaProcess[0], "运行");
            if (processExecResult.getExitCode() != 0) {
                log.error("运行错误：{}", processExecResult.getMessage());
                throw new RuntimeException("运行错误");
            }
            execResultList.add(processExecResult);
        }
        return execResultList;
    }

    /**
     * 4、整理输出结果
     * @param execResultList
     * @return 输出响应对象
     */
    public CodeSandBoxResponse getOutputResponse(List<ProcessExecResult> execResultList) {
        long maxTime = 0L;
        JudgeInfo judgeInfo = new JudgeInfo();
        List<String> outputList = new ArrayList<>();
        CodeSandBoxResponse response = new CodeSandBoxResponse();
        response.setStatus(0);

        for (ProcessExecResult processExecResult : execResultList) {
            if (processExecResult.getExitCode() != 0) {
                response.setStatus(processExecResult.getExitCode());
            }
            maxTime = Math.max(maxTime, processExecResult.getTime());
            outputList.add(processExecResult.getMessage());
        }

        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(null);  // 较麻烦，暂不实现

        response.setJudgeInfo(judgeInfo);
        response.setOutputs(outputList);

        return response;
    }

    /**
     * 5、删除用户代码
     * @param userCodeFile
     * @return 删除的结果
     */
    public boolean deleteFile(File userCodeFile) {
        String userCodeDirName = userCodeFile.getParentFile().getAbsolutePath();
        if (FileUtil.exist(userCodeDirName)) {
            return FileUtil.del(userCodeDirName);
        }

        return true;
    }

    /**
     * 获取错误响应
     * @param e
     * @return 错误响应结果
     */
    public CodeSandBoxResponse getErrorResponse(Exception e) {
        CodeSandBoxResponse response = new CodeSandBoxResponse();
        response.setMessage(e.getMessage());
        response.setOutputs(new ArrayList<>());
        response.setJudgeInfo(new JudgeInfo());
        response.setStatus(2);
        return response;
    }

    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {

        List<String> inputs = codeSandBoxRequest.getInputs();
        String language = codeSandBoxRequest.getLanguage();

        File userCodeFile = saveCode(codeSandBoxRequest);

        ProcessExecResult compiledResult = compileCode(userCodeFile);

        List<ProcessExecResult> execResultList = runCode(userCodeFile, inputs);

        CodeSandBoxResponse response = getOutputResponse(execResultList);

        boolean deleteFile = deleteFile(userCodeFile);
        if (!deleteFile) {
            log.warn("文件没有删除成功");
        }

        return response;
    }
}
