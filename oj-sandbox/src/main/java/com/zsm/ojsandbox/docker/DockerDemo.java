package com.zsm.ojsandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

/**
 * author: ZSM
 * time: 2025-03-05 8:36
 */
public class DockerDemo {
    public static void main(String[] args) throws InterruptedException {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://172.16.109.61:2375")
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);

//        dockerClient.removeImageCmd("hello-world").exec();

//        dockerClient.removeContainerCmd("focused_shannon")
//                .exec();

//        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
//            @Override
//            public void onNext(Frame item) {
//                System.out.println("日志：" + item.toString());
//                super.onNext(item);
//            }
//        };
//
//        dockerClient.logContainerCmd("924858a28820")
//                .withStdOut(true)
//                .withStdErr(true)
//                .exec(logContainerResultCallback)
//                .awaitCompletion();

//        StartContainerCmd startContainerCmd = dockerClient.startContainerCmd("focused_shannon");
//        startContainerCmd.exec();
//        System.out.println("启动成功");

//        Info info = dockerClient.infoCmd().exec();
//        System.out.println(info);
//        System.out.println(info.getImages());
//        System.out.println(info.getContainers());
//        System.out.println(info.getName());

//        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd("hello-world:latest");
//        createContainerCmd.withName("hello-world-container").exec();
//        System.out.println("创建成功");

//        PullImageCmd pullImageCmd = dockerClient.pullImageCmd("hello-world:latest");
//        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
//            @Override
//            public void onNext(PullResponseItem item) {
//                System.out.println(item.toString());
//                super.onNext(item);
//            }
//        };
//        pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
//        System.out.println("下载完成");
    }
}
