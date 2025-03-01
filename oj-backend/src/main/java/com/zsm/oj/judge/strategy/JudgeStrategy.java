package com.zsm.oj.judge.strategy;

import com.zsm.oj.model.dto.judge.JudgeInfo;

/**
 * 策略接口
 * author: ZSM
 * time: 2025-03-01 21:18
 */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeStrategyContext judgeStrategyContext);
}
