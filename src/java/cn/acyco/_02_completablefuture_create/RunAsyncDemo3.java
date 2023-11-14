package cn.acyco._02_completablefuture_create;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class RunAsyncDemo3 {
    public static void main(String[] args) {
        // 需求：使用CompletableFuture开启异步任务读取news.txt文件中的新闻稿，并打印输出。
        CommonUtils.printTheadLog("main start");

        CompletableFuture.runAsync(() -> {
            CommonUtils.printTheadLog("读取文件");
            String content = CommonUtils.readFile("news.txt");
            System.out.println(content);
        });

        CommonUtils.printTheadLog("here not blacked main continue");
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");
    }
}
