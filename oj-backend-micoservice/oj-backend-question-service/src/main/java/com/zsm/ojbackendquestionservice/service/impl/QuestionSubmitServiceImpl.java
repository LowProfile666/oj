package com.zsm.ojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsm.ojbackendcommon.common.ErrorCode;
import com.zsm.ojbackendcommon.exception.BusinessException;
import com.zsm.ojbackendmodel.dto.questionsubmit.QuestionSubmitAddRequest;
import com.zsm.ojbackendmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.zsm.ojbackendmodel.entity.Question;
import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import com.zsm.ojbackendmodel.entity.User;
import com.zsm.ojbackendmodel.enums.QuestionSubmitLanguageEnum;
import com.zsm.ojbackendmodel.enums.QuestionSubmitStatusEnum;
import com.zsm.ojbackendmodel.vo.QuestionSubmitVO;
import com.zsm.ojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.zsm.ojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.zsm.ojbackendquestionservice.service.QuestionService;
import com.zsm.ojbackendquestionservice.service.QuestionSubmitService;
import com.zsm.ojbackendserviceclient.JudgeFeignClient;
import com.zsm.ojbackendserviceclient.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;
    @Resource
    private MyMessageProducer myMessageProducer;

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

        Long questionSubmitId = questionSubmit.getId();
        // 向消息队列发送消息
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));

//        CompletableFuture.runAsync(() -> {
//            judgeFeignClient.doJudge(questionSubmitId);
//        });

        return questionSubmitId;
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据传来的对象，获取 mybatis 支持的查询 QueryWrapper 类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionSubmitQueryRequest.getId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        if (StringUtils.isNotBlank(sortField)) {
            if ("desc".equals(questionSubmitQueryRequest.getSortOrder())) {
                queryWrapper.orderByDesc(sortField);
            } else
                queryWrapper.orderByAsc(sortField);
        }

        return queryWrapper;
    }

    /**
     * 获取查询
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);

        if (!userFeignClient.isAdmin(loginUser) && !questionSubmit.getUserId().equals(loginUser.getId())) {
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser)).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




