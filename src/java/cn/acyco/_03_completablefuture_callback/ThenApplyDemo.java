package cn.acyco._03_completablefuture_callback;

import cn.acyco.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenApplyDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求：异步读取filter_words.txt文件中的内容，读取完成后，把内容转换成数组（敏感词数组），异步任务返回敏感词数组
        CommonUtils.printTheadLog("main start");

        CompletableFuture<String> readFileFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words文件");
            String filterWordContent = CommonUtils.readFile("filter_words.txt");
            return filterWordContent;

        });
        CompletableFuture<String[]> filterWordsFuture = readFileFuture.thenApply(content -> {
            CommonUtils.printTheadLog("把雇佣兵内容转换成敏感词数组");
            String[] filterWords = content.split(",");
            return filterWords;
        });

        CommonUtils.printTheadLog("main continue");
        String[] filterWords = filterWordsFuture.get();
        System.out.println("Arrays.toString(filterWords) = " + Arrays.toString(filterWords));
        CommonUtils.printTheadLog("main end");

        /**
         * 总线
         * thenApply(Function<T, R>) 可以对异步任务的结果进一步应该Function转换
         * 转换后的结果可以在主线程获取，也可以进行下一步的轮换。
         */
    }
}
