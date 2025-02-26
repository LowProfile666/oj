package com.zsm.oj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsm.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zsm.oj.model.entity.QuestionSubmit;
import com.zsm.oj.model.entity.User;

/**
* @author 20620
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-02-26 14:05:59
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
