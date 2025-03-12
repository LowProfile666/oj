package com.zsm.ojbackendserviceclient;


import com.zsm.ojbackendmodel.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 判题服务接口
 * author: ZSM
 * time: 2025-03-01 20:06
 */
@FeignClient(name = "oj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题
     * @param questionSubmitId 提交记录的
     * @return 提交信息
     */
    @PostMapping("do")
    QuestionSubmit doJudge(Long questionSubmitId);
}
