package com.zsm.ojbackendjudgeservice.controller.inner;

import com.zsm.ojbackendjudgeservice.service.JudgeService;
import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import com.zsm.ojbackendserviceclient.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author: ZSM
 * time: 2025-03-12 19:50
 */
@RequestMapping("/inner")
@RestController
public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("do")
    public QuestionSubmit doJudge(Long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
