package com.zsm.oj.model.dto.question;


import com.zsm.oj.model.dto.judge.JudgeCase;
import com.zsm.oj.model.dto.judge.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 题目创建请求
 */
@Data
public class QuestionAddRequest implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例（json 数组）
     */
    private JudgeCase judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}