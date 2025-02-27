package com.zsm.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsm.oj.model.dto.question.QuestionQueryRequest;
import com.zsm.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zsm.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zsm.oj.model.entity.Question;
import com.zsm.oj.model.entity.QuestionSubmit;
import com.zsm.oj.model.entity.User;
import com.zsm.oj.model.vo.QuestionSubmitVO;
import com.zsm.oj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

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
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
