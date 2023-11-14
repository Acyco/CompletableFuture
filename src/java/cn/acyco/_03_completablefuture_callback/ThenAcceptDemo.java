package cn.acyco._03_completablefuture_callback;

import cn.acyco.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ThenAcceptDemo {
    public static void main(String[] args) {
        // 需求：异步读取filter_words.txt文件中的内容，读取完成后，把内容转换成敏感词数组，然后打印敏感词数组
        CommonUtils.printTheadLog("main start");

        CompletableFuture.supplyAsync(()->{
            CommonUtils.printTheadLog("读取filter_words.txt文件");
            String filterWordsContent = CommonUtils.readFile("filter_words.txt");
            return filterWordsContent;
        }).thenApply(content ->{
            CommonUtils.printTheadLog("把文件内容转换成敏感词数组");
            String[] filterWords = content.split(",");
            return filterWords;
        }).thenAccept(filterWords->{
            CommonUtils.printTheadLog("filterWorlds = " + Arrays.toString(filterWords));
        });

        CommonUtils.printTheadLog("main continue");
        CommonUtils.sleepSecond(4);
        CommonUtils.printTheadLog("main end");

        /**
         * 总结：
         * thenAccept(Consumer<T> c) 可以对异步任务的结果进行消费使用
         * 返回一个不带结果的CompletableFuture对象
         */
    }
}
