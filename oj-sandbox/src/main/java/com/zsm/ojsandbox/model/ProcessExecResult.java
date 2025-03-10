package com.zsm.ojsandbox.model;

import lombok.Data;

/**
 * 执行命令后返回的结果
 * author: ZSM
 * time: 2025-03-02 15:39
 */
@Data
public class ProcessExecResult {
    /**
     * 状态码，0 为正常
     */
    private Integer exitCode;
    /**
     * 正常的输出结果
     */
    private String message;
    /**
     * 程序执行时间
     */
    private Long time;
    /**
     * 程序执行内存
     */
    private Long memory;
}
