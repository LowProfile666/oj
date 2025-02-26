package com.zsm.oj.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交消息枚举
 */
@Getter
public enum QuestionSubmitInfoEnum {
    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    COMPILE_ERROR("编译错误", "Compile Error"),
    MEMORY_LIMIT_EXCEEDED("内存溢出", "Memory Limit Exceeded"),
    TIME_LIMIT_EXCEEDED("超时", "Time Limit Exceeded"),
    PRESENTATION_ERROR("展示错误", "Presentation Error"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出", "Output Limit Exceeded"),
    WAITING("等待中", "Waiting"),
    DANGEROUS_OPERATION("危险操作", "Dangerous Operation"),
    RUNTIME_ERROR("运行错误", "Runtime Error"),
    SYSTEM_ERROR("系统错误", "System Error");

    private final String text;
    private final String value;

    QuestionSubmitInfoEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举对象
     */
    public static QuestionSubmitInfoEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (QuestionSubmitInfoEnum enumItem : QuestionSubmitInfoEnum.values()) {
            if (enumItem.value.equals(value)) {
                return enumItem;
            }
        }
        return null;
    }
}
