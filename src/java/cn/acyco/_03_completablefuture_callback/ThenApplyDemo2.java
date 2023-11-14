package cn.acyco._03_completablefuture_callback;

import cn.acyco.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenApplyDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求：异步读取filter_words.txt文件中的内容，读取完成后，把内容转换成数组（敏感词数组），异步任务返回敏感词数组
        CommonUtils.printTheadLog("main start");

        CompletableFuture<String[]> filterWordsFuture = CompletableFuture.supplyAsync(() -> {
            String filterWordContent = CommonUtils.readFile("filter_words.txt");
            return filterWordContent;
        }).thenApply(content -> {
            String[] filterWords = content.split(",");
            return filterWords;
        });

        CommonUtils.printTheadLog("main continue");
        String[] filterWords = filterWordsFuture.get();
        System.out.println("Arrays.toString(filterWords) = " + Arrays.toString(filterWords));
        CommonUtils.printTheadLog("main end");

    }
}
