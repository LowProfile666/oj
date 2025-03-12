package com.zsm.ojbackendjudgeservice.codesandbox.impl;


import com.zsm.ojbackendjudgeservice.codesandbox.CodeSandBox;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxRequest;
import com.zsm.ojbackendjudgeservice.codesandbox.model.CodeSandBoxResponse;
import com.zsm.ojbackendmodel.dto.judge.JudgeInfo;
import com.zsm.ojbackendmodel.enums.QuestionSubmitInfoEnum;

import java.util.List;

/**
 * 示例代码沙箱实现，为了测试跑通程序
 * author: ZSM
 * time: 2025-03-01 16:34
 */
public class ExampleSandBox implements CodeSandBox {
    @Override
    public CodeSandBoxResponse execute(CodeSandBoxRequest codeSandBoxRequest) {
        List<String> inputs = codeSandBoxRequest.getInputs();

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(QuestionSubmitInfoEnum.ACCEPTED.getText());
        judgeInfo.setTime(1000L);
        judgeInfo.setMemory(128L);

        return CodeSandBoxResponse.builder()
                .judgeInfo(judgeInfo)
                .outputs(inputs)
                .message("题目执行成功")
                .build();
    }
}
