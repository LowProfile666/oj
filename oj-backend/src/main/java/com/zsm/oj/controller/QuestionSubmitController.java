package com.zsm.oj.controller;

import com.zsm.oj.common.BaseResponse;
import com.zsm.oj.common.ErrorCode;
import com.zsm.oj.common.ResultUtils;
import com.zsm.oj.exception.BusinessException;
import com.zsm.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zsm.oj.model.entity.User;
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
     * 提交 / 取消提交
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
}
