package com.zsm.oj.model.dto.questionsubmit;

import com.zsm.oj.common.PageRequest;
import com.zsm.oj.model.dto.post.PostQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目提交查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}