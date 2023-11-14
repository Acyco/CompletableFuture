package cn.acyco._03_completablefuture_callback;

import cn.acyco.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.*;

public class ThenApplyAsyncDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //回顾： 异步读取filter_words.txt文件中的内容，读取完成后，转换成敏感词数组，主线程获取结果打印输出这个数组
        CommonUtils.printTheadLog("main start");
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletableFuture<String[]> filterWordFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words.txt文件");
            String filterWordsContent = CommonUtils.readFile("filter_words.txt");
            return filterWordsContent;
        }).thenApplyAsync(content -> {
            CommonUtils.printTheadLog("把文件内容转换成敏感词数组");
            String[] filterWords = content.split(",");
            return filterWords;
        },executor);
        CommonUtils.printTheadLog("main continue");
        String[] filterWords = filterWordFuture.get();
        CommonUtils.printTheadLog("filterWords = " + Arrays.toString(filterWords));

        executor.shutdown();
        CommonUtils.printTheadLog("main end");
    }
}
