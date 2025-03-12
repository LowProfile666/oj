package com.zsm.ojbackendjudgeservice.service.impl;

import cn.hutool.json.JSONUtil;
import com.zsm.ojbackendcommon.common.ErrorCode;
import com.zsm.ojbackendcommon.exception.BusinessException;
import com.zsm.ojbackendjudgeservice.codesandbox.CodeSandBox;
import com.zsm.ojbackendjudgeservice.codesandbox.CodeSandBoxFactory;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxProxy;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxRequest;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxResponse;
import com.zsm.ojbackendjudgeservice.manager.JudgeManager;
import com.zsm.ojbackendjudgeservice.service.JudgeService;
import com.zsm.ojbackendjudgeservice.strategy.JudgeStrategyContext;
import com.zsm.ojbackendmodel.dto.judge.JudgeCase;
import com.zsm.ojbackendmodel.dto.judge.JudgeInfo;
import com.zsm.ojbackendmodel.entity.Question;
import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import com.zsm.ojbackendmodel.enums.QuestionSubmitStatusEnum;
import com.zsm.ojbackendserviceclient.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: ZSM
 * time: 2025-03-01 20:08
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Value("${judge.codesandbox.type:example}")
    private String codesandboxType;
    @Resource
    private QuestionFeignClient questionFeignClient;
    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        // 获取提交记录信息
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交记录不存在");
        }

        // 判断当前是否是等待中状态
        Integer status = questionSubmit.getStatus();
        if (!QuestionSubmitStatusEnum.getEnumByValue(status).equals(QuestionSubmitStatusEnum.WAITING)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "重复判题操作");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目不存在");
        }

        // 将状态设置为判题中
        questionSubmit.setStatus(QuestionSubmitStatusEnum.JUDGING.getValue());
        questionFeignClient.updateQuestionSubmitById(questionSubmit);

        // 通过代码沙箱代理来调用代码沙箱
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(codesandboxType);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);

        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputs = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        CodeSandBoxRequest codeSandBoxRequest = CodeSandBoxRequest.builder()
                .language(language)
                .inputs(inputs)
                .code(code)
                .build();

        // 获取到判题结果信息
        CodeSandBoxResponse codeSandBoxResponse = codeSandBoxProxy.execute(codeSandBoxRequest);
        List<String> outputsByCodeBox = codeSandBoxResponse.getOutputs();
        JudgeInfo judgeInfoByCodeBox = codeSandBoxResponse.getJudgeInfo();

        JudgeStrategyContext judgeStrategyContext = new JudgeStrategyContext();
        judgeStrategyContext.setInputs(inputs);
        judgeStrategyContext.setOutputs(outputsByCodeBox);
        judgeStrategyContext.setJudgeInfo(judgeInfoByCodeBox);
        judgeStrategyContext.setQuestion(question);
        judgeStrategyContext.setQuestionSubmit(questionSubmit);
        judgeStrategyContext.setJudgeCases(judgeCases);

        JudgeInfo judgeInfoByManager = judgeManager.doJudge(judgeStrategyContext);

        // 更新提交记录
        questionSubmit.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoByManager));
        questionFeignClient.updateQuestionSubmitById(questionSubmit);

        return questionFeignClient.getQuestionSubmitById(questionSubmitId);
    }
}
