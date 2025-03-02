//public class Main {
//    public static void main(String[] args) {
//        int a = Integer.parseInt(args[0]);
//        int b = Integer.parseInt(args[1]);
//        System.out.println("输出结果是：" + (a + b));
//    }
//}

//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int a = sc.nextInt();
//        int b = sc.nextInt();
//        System.out.println("输出结果是：" + (a + b));
//    }
//}

//public class Main {
//    public static void main(String[] args) throws InterruptedException {
//        long ONE_HOUR = 60 * 60 * 1000L;
//        Thread.sleep(ONE_HOUR);
//        System.out.println("睡完了");
//    }
//}

//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 无限占用空间（浪费系统内存）
// */
//public class Main {
//
//    public static void main(String[] args) throws InterruptedException {
//        List<byte[]> bytes = new ArrayList<>();
//        while (true) {
//            bytes.add(new byte[10000]);
//        }
//    }
//}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


/**
 * 读取服务器文件（文件信息泄露）
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        String dir = System.getProperty("user.dir");
        String path = dir + File.separator + "src/main/resources/application.yml";
        List<String> strings = Files.readAllLines(Paths.get(path));
        System.out.println(String.join("\n", strings));

    }
}


//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Arrays;
//
///**
// * 向服务器写文件（植入危险程序）
// */
//public class Main {
//
//    public static void main(String[] args) throws InterruptedException, IOException {
//        String userDir = System.getProperty("user.dir");
//        String filePath = userDir + File.separator + "src/main/resources/muma.bat";
//        String errorProgram = "java -version 2>&1";
//        Files.write(Paths.get(filePath), Arrays.asList(errorProgram));
//        System.out.println("写木马成功，你完了哈哈");
//    }
//}

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
///**
// * author: ZSM
// * time: 2025-03-02 20:02
// */
//public class Main {
//    public static void main(String[] args) throws IOException {
//        String userDir = System.getProperty("user.dir");
//        String filePath = userDir + File.separator + "src/main/resources/muma.bat";
//        Process process = Runtime.getRuntime().exec(filePath);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String t = null;
//        while ((t = bufferedReader.readLine()) != null) {
//            System.out.println(t);
//        }
//    }
//}


//import java.io.IOException;
//
///**
// * 执行高危命令
// */
//public class Main {
//    public static void main(String[] args) throws IOException {
//        Runtime.getRuntime().exec("notepad.exe");
//    }
//}