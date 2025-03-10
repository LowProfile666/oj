package com.zsm.ojsandbox;

import com.zsm.ojsandbox.sercurity.DefaultSecurityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
class OjSandboxApplicationTests {

//    @Test
//    void testSecurity() throws IOException {
//        System.setSecurityManager(new DefaultSecurityManager());
//
//        String dir = System.getProperty("user.dir");
//        String path = dir + File.separator + "src/main/resources/muma.bat";
//        Files.write(Paths.get(path), "java --version".getBytes());
//    }
//
//    public static void main(String[] args) throws IOException {
//        System.setSecurityManager(new DefaultSecurityManager());
//
//        String dir = System.getProperty("user.dir");
//        String path = dir + File.separator + "src/main/resources/muma.bat";
//        Files.write(Paths.get(path), "java --version".getBytes());
//    }

}
