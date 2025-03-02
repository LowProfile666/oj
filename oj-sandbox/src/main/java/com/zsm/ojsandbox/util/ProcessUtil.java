package com.zsm.ojsandbox.util;

import com.zsm.ojsandbox.model.ProcessExecResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.Optional;

/**
 * author: ZSM
 * time: 2025-03-02 15:25
 */
@Slf4j
public class ProcessUtil {
    public static ProcessExecResult execCmdAndGetMessage(Process process, String opName) {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            // 获取编译命令的执行结果，0 为正常结束
            int exitCode = process.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder stringBuilder = new StringBuilder();
            if (exitCode != 0) {
                // 获取编译进程的错误输入流
                String t = null;
                while ((t = bufferedReader.readLine()) != null) {
                    stringBuilder.append(t).append("\n");
                }
                log.error("{}失败", opName);
                log.error("失败信息：{}", stringBuilder);
            } else {
                // 获取编译进程的输入流
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String t = null;
                while ((t = bufferedReader.readLine()) != null) {
                    stringBuilder.append(t).append("\n");
                }
                log.info("{}成功", opName);
            }

            stopWatch.stop();
            ProcessExecResult result = new ProcessExecResult();
            result.setExitCode(exitCode);
            result.setMessage(stringBuilder.toString());
            result.setTime(stopWatch.getTotalTimeMillis());

            System.out.println(stringBuilder);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ProcessExecResult execCmdAndGetMessage(Process process, String opName, String args) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bufferedWriter.write(args + "\n");
            bufferedWriter.flush();

            return execCmdAndGetMessage(process, opName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
