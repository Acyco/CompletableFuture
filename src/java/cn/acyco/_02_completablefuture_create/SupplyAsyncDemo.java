package cn.acyco._02_completablefuture_create;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class SupplyAsyncDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求： 开启异步任务读取news.txt文件中的新闻稿，返回文件中内容并在主线程打印输出
        CommonUtils.printTheadLog("main start");

        CompletableFuture<String> newsFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                String content = CommonUtils.readFile("news.txt");
                return content;
            }
        });

        CommonUtils.printTheadLog("here not blacked,main continue");
        // 阻塞并等待newsFuture完成
        String news = newsFuture.get();
        System.out.println("news = " + news);
        CommonUtils.printTheadLog("main end");
    }
}
