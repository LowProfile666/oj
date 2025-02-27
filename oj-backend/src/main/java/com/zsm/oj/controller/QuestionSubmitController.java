package com.zsm.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsm.oj.annotation.AuthCheck;
import com.zsm.oj.common.BaseResponse;
import com.zsm.oj.common.ErrorCode;
import com.zsm.oj.common.ResultUtils;
import com.zsm.oj.constant.UserConstant;
import com.zsm.oj.exception.BusinessException;
import com.zsm.oj.model.dto.question.QuestionQueryRequest;
import com.zsm.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zsm.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zsm.oj.model.entity.Question;
import com.zsm.oj.model.entity.QuestionSubmit;
import com.zsm.oj.model.entity.User;
import com.zsm.oj.model.vo.QuestionSubmitVO;
import com.zsm.oj.service.QuestionSubmitService;
import com.zsm.oj.service.QuestionService;
import com.zsm.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 提交变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                              HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取列表
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionQueryRequest, HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser(request);
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionQueryRequest));
        Page<QuestionSubmitVO> questionSubmitVOPage = questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser);
        return ResultUtils.success(questionSubmitVOPage);
    }
}
