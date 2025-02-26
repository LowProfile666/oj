package com.zsm.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsm.oj.common.ErrorCode;
import com.zsm.oj.exception.BusinessException;
import com.zsm.oj.mapper.QuestionSubmitMapper;
import com.zsm.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zsm.oj.model.entity.Question;
import com.zsm.oj.model.entity.QuestionSubmit;
import com.zsm.oj.model.entity.User;
import com.zsm.oj.model.enums.QuestionSubmitLanguageEnum;
import com.zsm.oj.model.enums.QuestionSubmitStatusEnum;
import com.zsm.oj.service.QuestionService;
import com.zsm.oj.service.QuestionSubmitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 20620
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2025-02-26 14:05:59
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        if (QuestionSubmitLanguageEnum.getEnumByValue(language) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean saveRes = this.save(questionSubmit);
        if (!saveRes) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交失败");
        }
        return questionSubmit.getId();
    }
}




