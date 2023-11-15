package cn.acyco._04_completablefuture_arrange;

import cn.acyco.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.*;

public class ThenComposeDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // thenCompose()
        // 需求：异步读取filter_words.txt文件中的内容，读取完成后，转换成敏感词数组待用
        CommonUtils.printTheadLog("main start");

        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletableFuture<String[]> filterWordsFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words文件");
            String filterWordsContent = CommonUtils.readFile("filter_words.txt");
            return filterWordsContent;
        }).thenComposeAsync(content -> CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("把内容转换成敏感词数组");
            String[] filterWords = content.split(",");
            return filterWords;
        }),executor);

        CommonUtils.printTheadLog("main continue");
        String[] filterWords = filterWordsFuture.get();
        CommonUtils.printTheadLog("filterWords = " + Arrays.toString(filterWords));
        // 关闭业务线程池
        executor.shutdown();
        CommonUtils.printTheadLog("main end");
    }
}
