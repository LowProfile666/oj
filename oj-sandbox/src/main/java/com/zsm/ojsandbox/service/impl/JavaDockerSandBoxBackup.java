package com.zsm.ojsandbox.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.zsm.ojsandbox.model.CodeSandBoxRequest;
import com.zsm.ojsandbox.model.CodeSandBoxResponse;
import com.zsm.ojsandbox.model.JudgeInfo;
import com.zsm.ojsandbox.model.ProcessExecResult;
import com.zsm.ojsandbox.service.CodeSandBox;
import com.zsm.ojsandbox.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * author: ZSM
 * time: 2025-03-02 13:25
 */
@Slf4j
public class JavaDockerSandBoxBackup implements CodeSandBox {
    // 用户代码的根路径的名字
    private final String USER_CODE_ROOT_DIR_NAME = "usercode";
    // 用户代码文件的名字
    private final String USER_CODE_FILE_NAME = "Main.java";
    // 超时时间
    private final long TTL = 5000;
    // Docker主机
    private static final String DOCKER_HOST = "tcp://172.16.109.61:2375";

    public static void main(String[] args) {
        JavaDockerSandBoxBackup javaNativeSandBox = new JavaDockerSandBoxBackup();
        CodeSandBoxRequest request = new CodeSandBoxRequest();

        String code = ResourceUtil.readStr("test/Main.java", StandardCharsets.UTF_8);

        request.setCode(code);
        request.setInputs(Arrays.asList("1 2", "3 4"));
        request.setLanguage("java");

        javaNativeSandBox.execute(request);
    }

    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {
        String code = codeSandBoxRequest.getCode();
        List<String> inputs = codeSandBoxRequest.getInputs();
        String language = codeSandBoxRequest.getLanguage();

        // 1、将代码保存为文件
        // 获取到项目中存放所有用户代码的根路径
        String userCodeRootDirPath = System.getProperty("user.dir") + File.separator + USER_CODE_ROOT_DIR_NAME;
        // 不存在则创建该目录
        if (!FileUtil.exist(userCodeRootDirPath)) {
            FileUtil.mkdir(userCodeRootDirPath);
        }

        // 设置单个用户存放代码的目录路径
        String userCodeDirName = userCodeRootDirPath + File.separator + UUID.randomUUID();
        String userCodeFilePath = userCodeDirName + File.separator + USER_CODE_FILE_NAME;
        System.out.println("文件路径：" + userCodeFilePath);
        // 将代码写到文件中去
        File userCodeFile = FileUtil.writeString(code, userCodeFilePath, StandardCharsets.UTF_8);

        // 2、编译代码
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ProcessUtil.execCmdAndGetMessage(compileProcess, "编译");
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        // 3、创建容器，把文件复制到容器中
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(DOCKER_HOST)
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);

        String image = "openjdk:21-jdk";

        List<Image> images = dockerClient.listImagesCmd().exec();
        boolean needInit = true;
        for (Image item : images) {
            if (item.getRepoTags()[0].equals(image)) {
                needInit = false;
                break;
            }
        }
        if (needInit) {
            System.out.println("拉取镜像");
            try {
                dockerClient.pullImageCmd(image).exec(new PullImageResultCallback()).awaitCompletion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("镜像拉取完毕");
        } else System.out.println("镜像已存在");

        // 创建容器，容器内的app目录绑定到用户代码的目录
        HostConfig hostConfig = new HostConfig();
        hostConfig.withBinds(new Bind(userCodeDirName, new Volume("/app")));
        hostConfig.withMemory(100 * 1024 * 1024L);
        hostConfig.withCpuCount(1L);

        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(image)
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(true)
//                .withCmd("/bin/sh")
                .exec();

        // 获得容器id
        String containerId = containerResponse.getId();

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        List<ProcessExecResult> execResultList = new ArrayList<>();
        long maxTime = 0L;
        // 在内部类中尽量使用引用变量
        long[] maxMemory = new long[1];
        for (String input : inputs) {
            long time = 0L;
            // 执行程序
            String[] inputArray = input.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArray);
            // 创建docker exec命令的对象
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();

            StopWatch stopWatch = new StopWatch();

            // 获取占用的内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void onNext(Statistics statistics) {
                    System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("内存错误：" + throwable);
                }

                @Override
                public void onComplete() {
                    System.out.println("完成了");
                }
            });
            statsCmd.exec(statisticsResultCallback);
            final String[] message = {""};
            final Integer[] exitCode = {0};
            final boolean[] isTimeOut = {false};
            final CountDownLatch latch = new CountDownLatch(1); // 初始化计数器为 1
            // 执行 docker exec 命令
            try {
                stopWatch.start();
                dockerClient.execStartCmd(execCreateCmdResponse.getId()).exec(
                                new ExecStartResultCallback() {
                                    @Override
                                    public void onNext(Frame frame) {
                                        String msg = new String(frame.getPayload(), StandardCharsets.UTF_8);
                                        if (frame.getStreamType().equals(StreamType.STDERR)) {
                                            exitCode[0] = 1;
                                        }
                                        message[0] = msg;
                                        System.out.println(msg);
                                        super.onNext(frame);
                                    }

                                    @Override
                                    public void onComplete() {
                                        System.out.println("代码执行完成了: " + Thread.currentThread().getName());
                                        latch.countDown(); // 计数器减 1
                                        super.onComplete();
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        System.out.println(throwable + ": " + Thread.currentThread().getName());
                                        isTimeOut[0] = true;
                                        exitCode[0] = 1;
                                        latch.countDown(); // 计数器减 1
                                        super.onError(throwable);
                                    }
                                })
                        .awaitCompletion(TTL, TimeUnit.MILLISECONDS);
                latch.await(); // 主线程等待计数器归零
                stopWatch.stop();
                statsCmd.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            time = stopWatch.getTotalTimeMillis();
            maxTime = Math.max(time, maxTime);

            ProcessExecResult processExecResult = new ProcessExecResult();
            processExecResult.setExitCode(exitCode[0]);
            processExecResult.setTime(time);
            processExecResult.setMemory(maxMemory[0]);
            processExecResult.setMessage(message[0]);

            if (isTimeOut[0]) {
                processExecResult.setTime(null);
                processExecResult.setMessage("超时");
            }

            execResultList.add(processExecResult);
        }

        // 4、封装结果
        CodeSandBoxResponse response = new CodeSandBoxResponse();
        List<String> outputs = new ArrayList<>();
        System.out.println(execResultList);
        for (ProcessExecResult processExecResult : execResultList) {
            if (processExecResult.getExitCode() != 0) {
                response.setMessage(processExecResult.getMessage());
                response.setStatus(processExecResult.getExitCode());
                break;
            }
            outputs.add(processExecResult.getMessage());
        }
        if (outputs.size() != inputs.size()) {
            response.setStatus(1);
        }

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage("");
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory[0]);

        response.setJudgeInfo(judgeInfo);
        response.setOutputs(outputs);

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
