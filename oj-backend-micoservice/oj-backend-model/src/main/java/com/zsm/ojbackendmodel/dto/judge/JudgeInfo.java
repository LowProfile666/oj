package com.zsm.ojbackendmodel.dto.judge;

import lombok.Data;

/**
 * 判题信息对象
 * author: ZSM
 * time: 2025-02-26 14:51
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 程序执行时间，ms
     */
    private Long time;
    /**
     * 程序执行内存，kb
     */
    private Long memory;
}
