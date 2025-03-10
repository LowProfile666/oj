package com.zsm.oj.judge.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.zsm.oj.judge.strategy.JudgeStrategy;
import com.zsm.oj.judge.strategy.JudgeStrategyContext;
import com.zsm.oj.model.dto.judge.JudgeCase;
import com.zsm.oj.model.dto.judge.JudgeConfig;
import com.zsm.oj.model.dto.judge.JudgeInfo;
import com.zsm.oj.model.entity.Question;
import com.zsm.oj.model.entity.QuestionSubmit;
import com.zsm.oj.model.enums.QuestionSubmitInfoEnum;
import com.zsm.oj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;
import java.util.Optional;

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
        Long memoryBySandBox = Optional.of(judgeInfoContext.getMemory()).orElse(0L);
        Long timeBySandBox = Optional.of(judgeInfoContext.getTime()).orElse(0L);

        Question question = judgeStrategyContext.getQuestion();
        JudgeConfig questionJudgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        Long timeLimit = Optional.ofNullable(questionJudgeConfig.getTimeLimit()).orElse(0L);
        Long memoryLimit = Optional.ofNullable(questionJudgeConfig.getMemoryLimit()).orElse(0L);
        Long stackLimit = Optional.ofNullable(questionJudgeConfig.getStackLimit()).orElse(0L);

        JudgeInfo judgeInfoToResponse = new JudgeInfo();
        judgeInfoToResponse.setMemory(memoryBySandBox);
        judgeInfoToResponse.setTime(timeBySandBox);

        // 定义一个判题结果枚举变量
        QuestionSubmitInfoEnum questionSubmitInfoEnum = QuestionSubmitInfoEnum.ACCEPTED;
        if (outputsContext.size() != inputsContext.size()) {
            questionSubmitInfoEnum = QuestionSubmitInfoEnum.WRONG_ANSWER;
        } else if (timeBySandBox > timeLimit) {
            questionSubmitInfoEnum = QuestionSubmitInfoEnum.TIME_LIMIT_EXCEEDED;
        } else if (memoryBySandBox > memoryLimit) {
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
