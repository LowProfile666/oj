package com.zsm.oj.judge.codesandbox.model.enums;

import lombok.Getter;

/**
 * author: ZSM
 * time: 2025-03-01 18:44
 */

@Getter
public enum CodeSandBoxTypeEnum {
    EXAMPLE("示例代码沙箱", "example"),
    REMOTE("远程代码沙箱", "remote"),
    THIRD("第三方代码沙箱", "third");

    private final String name;
    private final String value;

    CodeSandBoxTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static CodeSandBoxTypeEnum fromValue(String value) {
        for (CodeSandBoxTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null; // 或者抛出异常
    }
}
