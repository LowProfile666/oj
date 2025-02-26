package com.zsm.oj.model.dto.judge;

import lombok.Data;

/**
 * 判题配置信息
 * author: ZSM
 * time: 2025-02-26 14:42
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制，ms
     */
    private Long timeLimit;
    /**
     * 内存限制，kb
     */
    private Long memoryLimit;
    /**
     * 堆栈限制，kb
     */
    private Long stackLimit;
}
