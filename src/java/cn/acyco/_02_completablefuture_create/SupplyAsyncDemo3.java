package cn.acyco._02_completablefuture_create;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.*;

public class SupplyAsyncDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求： 开启异步任务读取news.txt文件中的新闻稿，返回文件中内容并在主线程打印输出
        CommonUtils.printTheadLog("main start");

        ExecutorService executor = Executors.newFixedThreadPool(4);
        // 使用Lambda表达式
        CompletableFuture<String> newsFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("异步读取文件开始");
            String news = CommonUtils.readFile("news.txt");
            return news;
        },executor);

        CommonUtils.printTheadLog("here not blacked,main continue");
        // 阻塞并等待newsFuture完成
        String news = newsFuture.get();
        System.out.println("news = " + news);
        // 关闭线程池
        executor.shutdown();
        CommonUtils.printTheadLog("main end");
    }
}
