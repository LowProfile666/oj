package com.zsm.oj.judge.codesandbox;

import com.zsm.oj.judge.codesandbox.impl.ExampleSandBox;
import com.zsm.oj.judge.codesandbox.impl.RemoteSandBox;
import com.zsm.oj.judge.codesandbox.impl.ThirdSandBox;
import com.zsm.oj.judge.codesandbox.model.enums.CodeSandBoxTypeEnum;

/**
 * 代码沙箱静态工厂
 * author: ZSM
 * time: 2025-03-01 18:43
 */
public class CodeSandBoxFactory {
    public static CodeSandBox createCodeSandBox(String type) {
        CodeSandBoxTypeEnum sandboxType = CodeSandBoxTypeEnum.fromValue(type);
        return switch (sandboxType) {
            case REMOTE -> new RemoteSandBox();
            case THIRD -> new ThirdSandBox();
            default -> new ExampleSandBox();
        };
    }
}
