package com.zsm.ojbackendjudgeservice.codesandbox;


import com.zsm.ojbackendjudgeservice.codesandbox.impl.ExampleSandBox;
import com.zsm.ojbackendjudgeservice.codesandbox.impl.RemoteSandBox;
import com.zsm.ojbackendjudgeservice.codesandbox.impl.ThirdSandBox;
import com.zsm.ojbackendmodel.codesandbox.enums.CodeSandBoxTypeEnum;

/**
 * 代码沙箱静态工厂
 * author: ZSM
 * time: 2025-03-01 18:43
 */
public class CodeSandBoxFactory {
    public static CodeSandBox createCodeSandBox(String type) {
        CodeSandBoxTypeEnum sandboxType = CodeSandBoxTypeEnum.fromValue(type);
        switch (sandboxType) {
            case REMOTE: return new RemoteSandBox();
            case THIRD: return new ThirdSandBox();
            default: return new ExampleSandBox();
        }
    }
}
