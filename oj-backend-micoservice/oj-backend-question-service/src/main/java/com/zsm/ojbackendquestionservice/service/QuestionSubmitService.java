package com.zsm.ojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsm.ojbackendmodel.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zsm.ojbackendmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import com.zsm.ojbackendmodel.entity.User;
import com.zsm.ojbackendmodel.vo.QuestionSubmitVO;

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

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
