package com.zsm.ojbackendserviceclient;

import com.zsm.ojbackendmodel.entity.Question;
import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author 20620
*/
@FeignClient(name = "oj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("question/get")
    Question getQuestionById(@RequestParam("id") Long id);

    @GetMapping("question_submit/get")
    QuestionSubmit getQuestionSubmitById(@RequestParam("id") Long id);

    @PostMapping("question_submit/update")
    Boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
