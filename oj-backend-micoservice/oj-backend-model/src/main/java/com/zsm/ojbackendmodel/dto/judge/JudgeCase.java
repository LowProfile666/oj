package com.zsm.ojbackendmodel.dto.judge;

import lombok.Data;

/**
 * 判题用例
 * author: ZSM
 * time: 2025-02-26 14:42
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;
}
