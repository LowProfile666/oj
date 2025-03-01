package com.zsm.oj.judge.service.impl;

import cn.hutool.json.JSONUtil;
import com.zsm.oj.common.ErrorCode;
import com.zsm.oj.exception.BusinessException;
import com.zsm.oj.judge.codesandbox.CodeSandBox;
import com.zsm.oj.judge.codesandbox.CodeSandBoxFactory;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxProxy;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxRequest;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxResponse;
import com.zsm.oj.judge.manager.JudgeManager;
import com.zsm.oj.judge.service.JudgeService;
import com.zsm.oj.judge.strategy.JudgeStrategy;
import com.zsm.oj.judge.strategy.JudgeStrategyContext;
import com.zsm.oj.judge.strategy.impl.DefaultJudgeStrategy;
import com.zsm.oj.judge.strategy.impl.JavaJudgeStrategy;
import com.zsm.oj.model.dto.judge.JudgeCase;
import com.zsm.oj.model.dto.judge.JudgeInfo;
import com.zsm.oj.model.entity.Question;
import com.zsm.oj.model.entity.QuestionSubmit;
import com.zsm.oj.model.enums.QuestionSubmitInfoEnum;
import com.zsm.oj.model.enums.QuestionSubmitLanguageEnum;
import com.zsm.oj.model.enums.QuestionSubmitStatusEnum;
import com.zsm.oj.service.QuestionService;
import com.zsm.oj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
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
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        // 获取提交记录信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交记录不存在");
        }

        // 判断当前是否是等待中状态
        Integer status = questionSubmit.getStatus();
        if (!QuestionSubmitStatusEnum.getEnumByValue(status).equals(QuestionSubmitStatusEnum.WAITING)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "重复判题操作");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目不存在");
        }

        // 将状态设置为判题中
        questionSubmit.setStatus(QuestionSubmitStatusEnum.JUDGING.getValue());
        questionSubmitService.updateById(questionSubmit);

        // 通过代码沙箱代理来调用代码沙箱
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(codesandboxType);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);

        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputs = judgeCases.stream().map(JudgeCase::getInput).toList();

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
        questionSubmitService.updateById(questionSubmit);

        return questionSubmitService.getById(questionSubmitId);
    }
}
