package com.zsm.ojbackendjudgeservice.manager;


import com.zsm.ojbackendjudgeservice.strategy.JudgeStrategy;
import com.zsm.ojbackendjudgeservice.strategy.JudgeStrategyContext;
import com.zsm.ojbackendjudgeservice.strategy.impl.DefaultJudgeStrategy;
import com.zsm.ojbackendjudgeservice.strategy.impl.JavaJudgeStrategy;
import com.zsm.ojbackendmodel.dto.judge.JudgeInfo;
import org.springframework.stereotype.Component;

/**
 * author: ZSM
 * time: 2025-03-01 22:59
 */
@Component
public class JudgeManager {
    public JudgeInfo doJudge(JudgeStrategyContext judgeStrategyContext) {
        String language = judgeStrategyContext.getQuestionSubmit().getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeStrategyContext);
    }
}
