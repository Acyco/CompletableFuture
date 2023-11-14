package cn.acyco._03_completablefuture_callback;

import cn.acyco.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class ThenRunDemo {
    public static void main(String[] args) {
        // 演示案例： 我们仅仅想知道敏感词汇的文件是否读取完成
        CommonUtils.printTheadLog("main start");

        CompletableFuture.supplyAsync(() -> {
            CommonUtils.printTheadLog("读取filter_words文件");
            String filterWordsContent = CommonUtils.readFile("filter_words.txt");
            return filterWordsContent;
        }).thenRun(() -> {
            CommonUtils.printTheadLog("读取filter_words文件读取完成");
        });

        CommonUtils.printTheadLog("main continue");
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");

        /**
         * thenRun(Runnable action);
         * 当异步任务完成后，只想得到一个完成的通知，不使用上一步异步任务的结果，就可以使用thenRun
         * 通过会把它用在链式操作的未端
         */
    }
}
