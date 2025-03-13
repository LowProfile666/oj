package com.zsm.ojbackendmodel.codesandbox;

import com.zsm.ojbackendmodel.dto.judge.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author: ZSM
 * time: 2025-03-01 16:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSandBoxResponse {
    /**
     * 代码沙箱的一些信息
     */
    private String message;
    /**
     * 程序的执行结果
     */
    private List<String> outputs;
    /**
     * 程序执行的一些信息
     */
    private JudgeInfo judgeInfo;
}
