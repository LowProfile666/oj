package com.zsm.oj.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交创建请求
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户代码
     */
    private String code;

    private static final long serialVersionUID = 1L;
}