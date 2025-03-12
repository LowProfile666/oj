package com.zsm.ojbackendjudgeservice.strategy;

import com.zsm.ojbackendmodel.dto.judge.JudgeCase;
import com.zsm.ojbackendmodel.dto.judge.JudgeInfo;
import com.zsm.ojbackendmodel.entity.Question;
import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 策略上下文对象，用于在策略中传递参数
 * author: ZSM
 * time: 2025-03-01 21:19
 */
@Data
public class JudgeStrategyContext {
    /**
     * 题目的输入列表
     */
    private List<String> inputs;
    /**
     * 代码沙箱返回的实际的输出列表
     */
    private List<String> outputs;
    /**
     * 代码沙箱返回的判题信息
     */
    private JudgeInfo judgeInfo;
    /**
     * 题目
     */
    private Question question;
    /**
     * 题目提交记录
     */
    private QuestionSubmit questionSubmit;
    /**
     * 题目的用例
     */
    private List<JudgeCase> judgeCases;
}
