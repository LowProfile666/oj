package com.zsm.oj.judge.strategy.impl;

import com.zsm.oj.judge.strategy.JudgeStrategy;
import com.zsm.oj.judge.strategy.JudgeStrategyContext;
import com.zsm.oj.model.dto.judge.JudgeCase;
import com.zsm.oj.model.dto.judge.JudgeInfo;
import com.zsm.oj.model.entity.Question;
import com.zsm.oj.model.entity.QuestionSubmit;
import com.zsm.oj.model.enums.QuestionSubmitInfoEnum;
import com.zsm.oj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 默认判题策略
 * author: ZSM
 * time: 2025-03-01 22:14
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeStrategyContext judgeStrategyContext) {
        List<String> inputsContext = judgeStrategyContext.getInputs();
        List<String> outputsContext = judgeStrategyContext.getOutputs();
        JudgeInfo judgeInfoContext = judgeStrategyContext.getJudgeInfo();

        List<JudgeCase> judgeCasesContext = judgeStrategyContext.getJudgeCases();
        List<String> outputs = judgeCasesContext.stream().map(JudgeCase::getOutput).toList();
        Long memoryBySandBox = judgeInfoContext.getMemory();
        Long timeBySandBox = judgeInfoContext.getTime();

        JudgeInfo judgeInfoToResponse = new JudgeInfo();
        judgeInfoToResponse.setMemory(memoryBySandBox);
        judgeInfoToResponse.setTime(timeBySandBox);

        // 定义一个判题结果枚举变量
        QuestionSubmitInfoEnum questionSubmitInfoEnum = QuestionSubmitInfoEnum.ACCEPTED;
        if (outputsContext.size() != inputsContext.size()) {
            questionSubmitInfoEnum = QuestionSubmitInfoEnum.WRONG_ANSWER;
        } else if (timeBySandBox > judgeInfoContext.getTime()) {
            questionSubmitInfoEnum = QuestionSubmitInfoEnum.TIME_LIMIT_EXCEEDED;
        } else if (memoryBySandBox > judgeInfoContext.getMemory()) {
            questionSubmitInfoEnum = QuestionSubmitInfoEnum.MEMORY_LIMIT_EXCEEDED;
        } else for (int i = 0; i < outputsContext.size(); i++) {
            if (!outputsContext.get(i).equals(outputs.get(i))) {
                questionSubmitInfoEnum = QuestionSubmitInfoEnum.WRONG_ANSWER;
                break;
            }
        }

        // 设置具体的判题结果信息
        judgeInfoToResponse.setMessage(questionSubmitInfoEnum.getText());

        // 返回判题信息
        return judgeInfoToResponse;
    }
}
