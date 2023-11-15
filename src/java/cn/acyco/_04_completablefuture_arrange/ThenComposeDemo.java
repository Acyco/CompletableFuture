package cn.acyco._04_completablefuture_arrange;

import cn.acyco.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenComposeDemo {
    public static CompletableFuture<String> readFileFuture(String fileName) {
        return CompletableFuture.supplyAsync(()->{
            String filterWordsContent = CommonUtils.readFile(fileName);
            return filterWordsContent;
        });
    }

    public static CompletableFuture<String[]> splitFuture(String content) {
        return CompletableFuture.supplyAsync(()->{
            String[] filterWords = content.split(",");
            return filterWords;
        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 编排2个依赖关系的异步任务 thenCompose()

        // 使用thenApply()
//        CompletableFuture<CompletableFuture<String[]>> future = readFileFuture("filter_words.txt").thenApply(conent -> {
//            return splitFuture(conent);
//        });

        // 使用thenCompose
        CompletableFuture<String[]> future = readFileFuture("filter_words.txt").thenCompose(content -> {
            return splitFuture(content);
        });
        String[] filterWords = future.get();
        CommonUtils.printTheadLog("filterWords = " + Arrays.toString(filterWords));

        /**
         * thenApply(Function<T,R>)
         * 重心在于对上一步异步任务的结果T进行应用转换，经Function回调转换后的结果R是一个简单的值
         *
         * thenCompose(Function<T, CompletableFuture<R>>)
         * 重心在于对上一步异步任务的结果T进行应用转换，经Function回调转换后的结果CompletableFuture对象
         *
         */
    }
}
