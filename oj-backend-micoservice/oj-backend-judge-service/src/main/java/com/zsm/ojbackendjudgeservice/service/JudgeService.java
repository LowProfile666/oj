package com.zsm.ojbackendjudgeservice.service;


import com.zsm.ojbackendmodel.entity.QuestionSubmit;

/**
 * 判题服务接口
 * author: ZSM
 * time: 2025-03-01 20:06
 */
public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId 提交记录的id
     * @return 提交信息
     */
    QuestionSubmit doJudge(Long questionSubmitId);
}
