package com.zsm.oj.judge.codesandbox.model;

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
public class CodeSandBoxRequest {
    /**
     * 代码
     */
    private String code;
    /**
     * 输入用例
     */
    private List<String> inputs;
    /**
     * 编程语言
     */
    private String language;
}
