package com.zsm.oj.judge.codesandbox;

import com.zsm.oj.judge.codesandbox.impl.ExampleSandBox;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxProxy;
import com.zsm.oj.judge.codesandbox.model.CodeSandBoxRequest;
import com.zsm.oj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * author: ZSM
 * time: 2025-03-01 16:57
 */
@SpringBootTest
class CodeSandBoxTest {

    @Value("${judge.codesandbox.type:example}")
    private String type;

    @Test
    void executeByValue() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);
        String code = """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("hello world");
                    }
                }
                """;
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputs = Arrays.asList("12 21", "1 2");
        CodeSandBoxRequest request = CodeSandBoxRequest.builder()
                .language(language)
                .inputs(inputs)
                .code(code)
                .build();

        System.out.println(codeSandBox.execute(request));
    }

    @Test
    void executeByProxy() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        String code = """
                public class Main {
                    public static void main(String[] args) {
                        int a = Integer.parseInt(args[0]);
                        int b = Integer.parseInt(args[1]);
                        System.out.println(a + b);
                    }
                }
                """;
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputs = Arrays.asList("12 21", "1 2");
        CodeSandBoxRequest request = CodeSandBoxRequest.builder()
                .language(language)
                .inputs(inputs)
                .code(code)
                .build();

        System.out.println(codeSandBoxProxy.execute(request));
    }

    @Test
    void execute() {
        CodeSandBox codeSandBox = new ExampleSandBox();
        String code = """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("hello world");
                    }
                }
                """;
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputs = Arrays.asList("12 21", "1 2");
        CodeSandBoxRequest request = CodeSandBoxRequest.builder()
                .language(language)
                .inputs(inputs)
                .code(code)
                .build();

        System.out.println(codeSandBox.execute(request));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            CodeSandBox codeSandBox = CodeSandBoxFactory.createCodeSandBox(input);
            System.out.println(codeSandBox.execute(new CodeSandBoxRequest()));
        }
    }
}