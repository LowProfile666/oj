package com.zsm.ojbackendjudgeservice.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.zsm.ojbackendjudgeservice.strategy.JudgeStrategy;
import com.zsm.ojbackendjudgeservice.strategy.JudgeStrategyContext;
import com.zsm.ojbackendmodel.dto.judge.JudgeCase;
import com.zsm.ojbackendmodel.dto.judge.JudgeConfig;
import com.zsm.ojbackendmodel.dto.judge.JudgeInfo;
import com.zsm.ojbackendmodel.entity.Question;
import com.zsm.ojbackendmodel.enums.QuestionSubmitInfoEnum;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * author: ZSM
 * time: 2025-03-01 22:53
 */
public class JavaJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeStrategyContext judgeStrategyContext) {
        List<String> inputsContext = judgeStrategyContext.getInputs();
        List<String> outputsContext = judgeStrategyContext.getOutputs();
        JudgeInfo judgeInfoContext = judgeStrategyContext.getJudgeInfo();

        Question question = judgeStrategyContext.getQuestion();
        JudgeConfig questionJudgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        Long timeLimit = Optional.ofNullable(questionJudgeConfig.getTimeLimit()).orElse(0L);
        Long memoryLimit = Optional.ofNullable(questionJudgeConfig.getMemoryLimit()).orElse(0L);
        Long stackLimit = Optional.ofNullable(questionJudgeConfig.getStackLimit()).orElse(0L);

        List<JudgeCase> judgeCasesContext = judgeStrategyContext.getJudgeCases();
        List<String> outputs = judgeCasesContext.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        Long memoryBySandBox = Optional.ofNullable(judgeInfoContext.getMemory()).orElse(0L);
        Long timeBySandBox = Optional.ofNullable(judgeInfoContext.getTime()).orElse(0L);

        JudgeInfo judgeInfoToResponse = new JudgeInfo();
        judgeInfoToResponse.setMemory(memoryBySandBox);
        judgeInfoToResponse.setTime(timeBySandBox);

        Long JAVA_MORE_TIME = 10 * 1000L;
        // 定义一个判题结果枚举变量
        QuestionSubmitInfoEnum questionSubmitInfoEnum = QuestionSubmitInfoEnum.ACCEPTED;
        if (outputsContext.size() != inputsContext.size()) {
            questionSubmitInfoEnum = QuestionSubmitInfoEnum.WRONG_ANSWER;
        } else if ((timeBySandBox - JAVA_MORE_TIME) > timeLimit) {
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
