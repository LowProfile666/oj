package com.zsm.ojbackendquestionservice.controller.inner;

import com.zsm.ojbackendmodel.entity.Question;
import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import com.zsm.ojbackendquestionservice.service.QuestionService;
import com.zsm.ojbackendquestionservice.service.QuestionSubmitService;
import com.zsm.ojbackendserviceclient.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * author: ZSM
 * time: 2025-03-12 19:41
 */
@RequestMapping("/inner")
@RestController
public class QuestionInnerController implements QuestionFeignClient {
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("question/get")
    public Question getQuestionById(@RequestParam("id") Long id) {
        return questionService.getById(id);
    }

    @Override
    @GetMapping("question_submit/get")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("id") Long id) {
        return questionSubmitService.getById(id);
    }

    @Override
    @PostMapping("question_submit/update")
    public Boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
